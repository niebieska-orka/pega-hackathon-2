package niebieska.orka.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import niebieska.orka.server.data.CharacterType;
import niebieska.orka.server.data.Child;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
    private static final AtomicInteger counter = new AtomicInteger(1);
    private final Map<String, Child> children = new HashMap<>();
    private MqttClient client;

    //when sending, add "/{ID}" to topic
    public static void main(String... args) throws Exception {
        new Server().start();
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
        while (true) {
            System.out.println("Idling...");
            Thread.sleep(5000);
        }
    }

    private void processParentStatusRequest(String topic, MqttMessage message) {
        // basically like processChildStatusRequest, just different tasks
    }

    private void createChild(String topic, MqttMessage message) throws IOException, MqttException {
        final ObjectNode node = new ObjectMapper().readValue(message.getPayload(), ObjectNode.class);
        String id = String.valueOf(counter.incrementAndGet());
        String childUsername = node.get("child_username").asText();
        String parentUsername = node.get("parent_username").asText();
        Child child = new Child(id, childUsername, parentUsername, new CharacterType());
        children.put(id, child);
        client.publish(CHILD_CREATION_ANSWER_TOPIC + "/" + childUsername + "/" + parentUsername,
                new MqttMessage(id.getBytes()));
    }

    private void processChildStatusRequest(String topic, MqttMessage message) throws IOException {
        final ObjectNode node = new ObjectMapper().readValue(message.getPayload(), ObjectNode.class);
        String id = node.get("id").asText();
        Child child = children.get(id);
        if (child == null) {
            return;
        }
        // extract tasks that need to be sent
        // convert them to json
        // send tasks to child
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.createObjectNode();
    }

    private void processChildAction(String topic, MqttMessage message) {
        System.out.println(topic + ": " + message);
        // extract task from request
        // update task status etc
    }

    private void processParentAction(String topic, MqttMessage message) {
        System.out.println(topic + ": " + message);
        // extract task from request
        // update task status etc
    }
}
