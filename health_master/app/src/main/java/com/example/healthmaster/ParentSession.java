package com.example.healthmaster;

import android.content.Context;
import android.util.Base64;

import com.example.healthmaster.model.Status;
import com.example.healthmaster.model.Task;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class ParentSession {
    private static final String BROKER = "tcp://192.168.43.126:1883";
    private static final String TOPIC_PREFIX = "niebieska/orka/health/hero";
    private static final String PARENT_STATUS_REQUEST_TOPIC = TOPIC_PREFIX + "/parent/status/request";
    private static final String PARENT_STATUS_UPDATE_TOPIC = TOPIC_PREFIX + "/parent/status/update";
    private static final String PARENT_ACTION_TO_SERVER_TOPIC = TOPIC_PREFIX + "/action/parent/server";
    private static final String PARENT_ADD_TASK_TOPIC = TOPIC_PREFIX + "/task/add";
    private static final int STATUS_PING = 700;
    private static ParentSession parentSession;
    private final Context context;
    private final MqttAndroidClient client;

    private final String id;
    private Map<String, Task> childTasks = new HashMap<>();
    private List<Task> exampleTasks = new ArrayList<>();
    private Timer timer;

    private ParentSession(Context context) throws MqttException {
        this.context = context;
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(context, BROKER, clientId, new MemoryPersistence(), MqttAndroidClient.Ack.AUTO_ACK);
        exampleTasks.add(new Task(new Timestamp(System.currentTimeMillis() + 100000L), "Watch the air", "Check the current air pollution levels. If the air is too toxic, you need to wear a mask!", 50));
        exampleTasks.add(new Task(new Timestamp(System.currentTimeMillis() + 100000000L), "Greens are good for you!", "Eat a green vegetable!", 10));
        Task childTask1 = new Task(new Timestamp(System.currentTimeMillis() + 1000000L), "Eat your carrots", "I prepared them for you with all my love <3", 100);
        childTask1.setStatus(Status.TO_CONFIRM);
        childTask1.setId("22334");
        Task childTask2 = new Task(new Timestamp(System.currentTimeMillis() + 8000000L), "Wear a proper hat", "It's quite cold today. Remember about your hat!", 50);
        childTask2.setStatus(Status.TO_CONFIRM);
        childTask2.setId("223345");
        childTasks.put("22334", childTask1);
        childTasks.put("223345", childTask2);
        id = "2";
        client.connect(context, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                try {
                    Thread.sleep(800);
                    subscribeForStatusUpdates();
                    scheduleTaskUpdatePings();
                } catch (InterruptedException | MqttException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    private void subscribeForStatusUpdates() throws MqttException {
        client.subscribe(PARENT_STATUS_UPDATE_TOPIC + "/" + id, 1, new IMqttMessageListener() {
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                JSONObject statusUpdateNode = new JSONObject(new String(message.getPayload()));
                JSONArray changedTasksArray = statusUpdateNode.getJSONArray("changed_tasks");
                for (int i = 0; i < changedTasksArray.length(); i++) {
                    JSONObject taskNode = changedTasksArray.getJSONObject(i);
                    String taskId = taskNode.getString("id");
                    Task task = childTasks.get(taskId);
                    task.setContent(Base64.decode(taskNode.getString("content"), Base64.DEFAULT));
                    task.setStatus(Status.TO_CONFIRM);
                }
            }
        });
    }

    private void scheduleTaskUpdatePings() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    JSONObject statusRequest = new JSONObject();
                    statusRequest.put("id", id);
                    client.publish(PARENT_STATUS_REQUEST_TOPIC, new MqttMessage(statusRequest.toString().getBytes()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, 800, STATUS_PING);
    }


    public static ParentSession getInstance(Context context) {
        if (parentSession == null) {
            try {
                parentSession = new ParentSession(context);
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        }
        return parentSession;
    }

    public void setTaskStatus(String taskId, boolean isAccepted) throws JSONException, MqttException {
        Task task = childTasks.get(taskId);
        task.setStatus(isAccepted ? Status.COMPLETED : Status.FAILED);
        JSONObject updateNode = new JSONObject();
        updateNode.put("task_id", taskId);
        updateNode.put("child_id", id);
        updateNode.put("accepted", String.valueOf(isAccepted));
        client.publish(PARENT_ACTION_TO_SERVER_TOPIC, new MqttMessage(updateNode.toString().getBytes()));
    }

    public void addTaskForChild(Task task) throws MqttException, JSONException {
        childTasks.put(task.getId(), task);
        task.setStatus(Status.TO_DO);
        JSONObject taskNode = new JSONObject();
        taskNode.put("child_id", id);
        taskNode.put("deadline", task.getDeadline().getTime());
        taskNode.put("name", task.getName());
        taskNode.put("description", task.getDescription());
        taskNode.put("xp", task.getXp());

        client.publish(PARENT_ADD_TASK_TOPIC, new MqttMessage(taskNode.toString().getBytes()));
    }

    public List<Task> getTaskTemplates() {
        return this.exampleTasks;
    }

    public List<Task> getDoneTasks() {
        List<Task> doneTasks = new ArrayList<>();
        for (Task task : childTasks.values()) {
            if (task.getStatus().equals(Status.TO_CONFIRM)) {
                doneTasks.add(task);
            }
        }
        return doneTasks;
    }
}
