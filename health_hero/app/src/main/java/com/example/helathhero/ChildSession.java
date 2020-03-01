package com.example.helathhero;

import android.content.Context;
import android.util.Base64;

import com.example.helathhero.model.Child;
import com.example.helathhero.model.Status;
import com.example.helathhero.model.Task;

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
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChildSession {
    private static final String BROKER = "tcp://192.168.43.126:1883";
    private static final String TOPIC_PREFIX = "niebieska/orka/health/hero";
    private static final String CHILD_CREATION_REQUEST_TOPIC = TOPIC_PREFIX + "/create";
    private static final String CHILD_CREATION_ANSWER_TOPIC = TOPIC_PREFIX + "/create/result";
    private static final String CHILD_STATUS_REQUEST_TOPIC = TOPIC_PREFIX + "/child/status/request";
    private static final String CHILD_STATUS_UPDATE_TOPIC = TOPIC_PREFIX + "/child/status/update";
    private static final String CHILD_ACTION_TO_SERVER_TOPIC = TOPIC_PREFIX + "/action/child/server";
    private static final int STATUS_PING = 700;
    private static ChildSession session;
    private final MqttAndroidClient client;
    private final Context context;
    private final Child child = new Child();
    private Timer timer;

    private ChildSession(Context context) throws MqttException, JSONException {
        this.context = context;
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(context, BROKER, clientId, new MemoryPersistence(), MqttAndroidClient.Ack.AUTO_ACK);
        final String childUsername = "Jasio";
        child.setCharacterName(childUsername);
        final String parentUsername = "Mama Jasia";
        client.connect(context, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                try {
                    register(childUsername, parentUsername);
                    Thread.sleep(800);
                    subscribeForStatusUpdates();
                    scheduleTaskUpdatePings();
                } catch (MqttException | JSONException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    private void scheduleTaskUpdatePings() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    JSONObject statusRequest = new JSONObject();
                    statusRequest.put("id", child.getId());
                    client.publish(CHILD_STATUS_REQUEST_TOPIC, new MqttMessage(statusRequest.toString().getBytes()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, 800, STATUS_PING);
    }

    private void subscribeForStatusUpdates() throws MqttException {
        client.subscribe(CHILD_STATUS_UPDATE_TOPIC + "/" + child.getId(), 1, new IMqttMessageListener() {
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println("Got update!!!" + message.toString());
                JSONObject statusUpdateNode = new JSONObject(new String(message.getPayload()));
                JSONArray changedTasksArray = statusUpdateNode.getJSONArray("changed_tasks");
                for (int i = 0; i < changedTasksArray.length(); i++) {
                    JSONObject taskNode = changedTasksArray.getJSONObject(i);
                    String taskId = taskNode.getString("id");
                    if (child.getTask(taskId) == null) {
                        child.addTask(taskId,
                                new Timestamp(taskNode.getLong("deadline")),
                                taskNode.getString("name"),
                                taskNode.getString("description"),
                                taskNode.getInt("xp"),
                                Status.valueOf(taskNode.getString("status")));
                    } else {
                        child.getTask(taskId).setStatus(Status.valueOf(taskNode.getString("status")));
                    }
                    child.setLevel(taskNode.getInt("new_level"));
                    child.setXp(taskNode.getInt("xp"));
                }
            }
        });
    }

    private void register(final String childUsername, final String parentUsername) throws MqttException, JSONException {
        System.out.println("Registering...");
        client.subscribe(CHILD_CREATION_ANSWER_TOPIC + "/" + childUsername + "/" + parentUsername, 1, new IMqttMessageListener() {
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                JSONObject id = new JSONObject(new String(message.getPayload()));
                child.setId(id.getString("id"));
                System.out.println("Registered! id: " + child.getId());
            }
        });
        JSONObject registrationRequest = new JSONObject();
        registrationRequest.put("child_username", childUsername);
        registrationRequest.put("parent_username", parentUsername);
        client.publish(CHILD_CREATION_REQUEST_TOPIC, new MqttMessage(registrationRequest.toString().getBytes()));
    }

    public static ChildSession getInstance(Context context) {
        if (session == null) {
            try {
                session = new ChildSession(context);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return session;
    }

    public synchronized List<Task> getTasks() {
        return new ArrayList<>(child.getTasks());
    }

    public Task getTask(String id) {
        return child.getTask(id);
    }

    public void progressTask(String taskId, byte[] content) throws JSONException, MqttException {
        Task task = child.getTask(taskId);
        task.setStatus(Status.TO_CONFIRM);
        JSONObject taskUpdateNode = new JSONObject();
        taskUpdateNode.put("child_id", child.getId());
        taskUpdateNode.put("task_id", taskId);
        taskUpdateNode.put("content", Base64.encodeToString(content, Base64.DEFAULT));
        System.out.println(taskUpdateNode.getString("content"));
        client.publish(CHILD_ACTION_TO_SERVER_TOPIC, new MqttMessage(taskUpdateNode.toString().getBytes()));
    }

    public Child getChild() {
        return this.child;
    }
}
