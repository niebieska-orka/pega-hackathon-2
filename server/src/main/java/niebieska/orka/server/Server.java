package niebieska.orka.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import niebieska.orka.server.data.CharacterType;
import niebieska.orka.server.data.Child;
import niebieska.orka.server.data.Status;
import niebieska.orka.server.data.Task;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private static final String BROKER = "tcp://192.168.43.126:1883";
    private static final String TOPIC_PREFIX = "niebieska/orka/health/hero";
    private static final String CHILD_CREATION_REQUEST_TOPIC = TOPIC_PREFIX + "/create";
    private static final String CHILD_CREATION_ANSWER_TOPIC = TOPIC_PREFIX + "/create/result";
    private static final String CHILD_STATUS_REQUEST_TOPIC = TOPIC_PREFIX + "/child/status/request";
    private static final String CHILD_STATUS_UPDATE_TOPIC = TOPIC_PREFIX + "/child/status/update";
    private static final String PARENT_STATUS_REQUEST_TOPIC = TOPIC_PREFIX + "/parent/status/request";
    private static final String PARENT_STATUS_UPDATE_TOPIC = TOPIC_PREFIX + "/parent/status/update";
    private static final String CHILD_ACTION_TO_SERVER_TOPIC = TOPIC_PREFIX + "/action/child/server";
    private static final String PARENT_ACTION_TO_SERVER_TOPIC = TOPIC_PREFIX + "/action/parent/server";
    private static final String PARENT_ADD_TASK_TOPIC = TOPIC_PREFIX + "/task/add";
    private static final AtomicInteger counter = new AtomicInteger(1);
    private final Map<String, Child> children = new HashMap<>();
    private MqttClient client;

    //when sending, add "/{ID}" to topic
    public static void main(String... args) throws Exception {
        new Server().start();
    }

    private static Collection<JsonNode> convertTasksToJsonNodes(List<Task> tasksToSend, ObjectMapper mapper) {
        Collection<JsonNode> taskNodes = new ArrayList<>(tasksToSend.size());
        tasksToSend.forEach(task -> {
            ObjectNode taskNode = mapper.createObjectNode();
            taskNode.put("id", task.getId());
            taskNode.put("name", task.getName());
            taskNode.put("description", task.getDescription());
            taskNode.put("deadline", task.getDeadline().getTime());
            taskNode.put("xp", task.getXp());
            taskNode.put("status", task.getStatus().name());
            taskNode.put("content", Base64.getEncoder().encode(task.getContent()));
            taskNodes.add(taskNode);
        });
        return taskNodes;
    }

    private void start() throws MqttException, InterruptedException {
        client = new MqttClient(BROKER, "niebieska.orka.health.hero.server", new MemoryPersistence());
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        System.out.println("Connecting to broker: " + BROKER);
        client.connect(connOpts);
        System.out.println("Connected");
        client.subscribe(CHILD_STATUS_REQUEST_TOPIC, 1, this::processChildStatusRequest);
        client.subscribe(PARENT_STATUS_REQUEST_TOPIC, 1, this::processParentStatusRequest);
        client.subscribe(CHILD_ACTION_TO_SERVER_TOPIC, 1, this::processChildAction);
        client.subscribe(PARENT_ACTION_TO_SERVER_TOPIC, 1, this::processParentAction);
        client.subscribe(CHILD_CREATION_REQUEST_TOPIC, 1, this::createChild);
        client.subscribe(PARENT_ADD_TASK_TOPIC, 1, this::addTask);
        while (true) {
            System.out.println("Idling...");
            Thread.sleep(5000);
        }
    }

    private void addTask(String topic, MqttMessage message) throws IOException {
        final ObjectNode node = new ObjectMapper().readValue(message.getPayload(), ObjectNode.class);
        String childId = node.get("child_id").asText();
        String taskId = String.valueOf(counter.incrementAndGet());
        Timestamp deadline = new Timestamp(node.get("deadline").asLong());
        String name = node.get("name").asText();
        String description = node.get("description").asText();
        int xp = node.get("xp").asInt();
        children.get(childId).addTask(taskId, deadline, name, description, xp);
    }

    private void processParentStatusRequest(String topic, MqttMessage message) throws IOException, MqttException {
        System.out.println("processing parent request: " + message.toString());
        final ObjectNode node = new ObjectMapper().readValue(message.getPayload(), ObjectNode.class);
        String id = node.get("id").asText();
        System.out.println("parent asked! " + id);
        Child child = children.get(id);
        if (child == null) {
            return;
        }
        List<Task> tasksToSend = child.getParentUpdate();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        Collection<JsonNode> taskNodes = convertTasksToJsonNodes(tasksToSend, mapper);
        rootNode.putArray("changed_tasks").addAll(taskNodes);
        client.publish(PARENT_STATUS_UPDATE_TOPIC + "/" + id,
                new MqttMessage(mapper.writeValueAsString(rootNode).getBytes()));
    }

    private void createChild(String topic, MqttMessage message) throws IOException, MqttException {
        final ObjectNode node = new ObjectMapper().readValue(message.getPayload(), ObjectNode.class);
        String id = String.valueOf(counter.incrementAndGet());
        String childUsername = node.get("child_username").asText();
        String parentUsername = node.get("parent_username").asText();
        Child child = new Child(id, childUsername, parentUsername, new CharacterType());
        children.put(id, child);
        ObjectNode idNode = new ObjectMapper().createObjectNode();
        idNode.put("id", id);
        System.out.println(new ObjectMapper().writeValueAsString(idNode));
        child.addTask(String.valueOf(counter.incrementAndGet()), new Timestamp(1583004706019L), "Eat a carrot", "Every healthy hero knows that carrots let you see in the dark! Harness the power of the shadows!!!!", 20);
        child.addTask(String.valueOf(counter.incrementAndGet()), new Timestamp(1583014706019L), "Wear a hat", "It's cold. Can you imagine a ninja that sneezes and gives away their position? Well we can't!", 30);
        String taskId = String.valueOf(counter.incrementAndGet());
        child.addTask(taskId, new Timestamp(1583034706019L), "Kiss your neighbor's dog", "Sometimes we need to suffer in the name of the basic virus immunity. So go and kiss that filthy dog!", 100);
        child.setTaskStatus(taskId, Status.COMPLETED);
        client.publish(CHILD_CREATION_ANSWER_TOPIC + "/" + childUsername + "/" + parentUsername,
                new MqttMessage(new ObjectMapper().writeValueAsString(idNode).getBytes()));
    }

    private void processChildStatusRequest(String topic, MqttMessage message) throws IOException, MqttException {
        System.out.println("child request! " + new String(message.getPayload()));
        final ObjectNode node = new ObjectMapper().readValue(message.getPayload(), ObjectNode.class);
        String id = node.get("id").asText();
        Child child = children.get(id);
        if (child == null) {
            return;
        }
        List<Task> tasksToSend = child.getChildUpdateAndUpdateStatus();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        Collection<JsonNode> taskNodes = convertTasksToJsonNodes(tasksToSend, mapper);
        rootNode.putArray("changed_tasks").addAll(taskNodes);
        rootNode.put("new_xp", child.getXP());
        rootNode.put("new_level", child.getLevel());
        System.out.println("topic:" + CHILD_STATUS_UPDATE_TOPIC + "/" + id);
        client.publish(CHILD_STATUS_UPDATE_TOPIC + "/" + id,
                new MqttMessage(mapper.writeValueAsString(rootNode).getBytes()));
    }

    private void processChildAction(String topic, MqttMessage message) throws IOException {
        System.out.println("child action!" + message.toString());
        final ObjectNode node = new ObjectMapper().readValue(message.getPayload(), ObjectNode.class);
        String taskId = node.get("task_id").asText();
        String childId = node.get("child_id").asText();
        String content = node.get("content").asText();
        children.get(childId).addTaskContent(taskId, Base64.getDecoder().decode(content));
        children.get(childId).setTaskStatus(taskId, Status.TO_CONFIRM);
    }

    private void processParentAction(String topic, MqttMessage message) throws IOException {
        System.out.println("parent action!" + message.toString());
        final ObjectNode node = new ObjectMapper().readValue(message.getPayload(), ObjectNode.class);
        String taskId = node.get("task_id").asText();
        String childId = node.get("child_id").asText();
        boolean isAccepted = node.get("accepted").asBoolean();
        Child child = children.get(childId);
        child.setTaskStatus(taskId, isAccepted ? Status.COMPLETED : Status.FAILED);
    }
}
