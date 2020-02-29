package com.example.helathhero;

import android.content.Context;
import android.util.Base64;

import com.example.helathhero.model.Child;
import com.example.helathhero.model.Status;
import com.example.helathhero.model.Task;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Collection;
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

        register(childUsername, parentUsername);
        subscribeForTastUpdates();
        scheduleTaskUpdatePings();
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
        }, 0, STATUS_PING);
    }

    private void subscribeForTastUpdates() throws MqttException {
        client.subscribe(CHILD_STATUS_UPDATE_TOPIC + "/" + child.getId(), 1, new IMqttMessageListener() {
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                JSONObject statusUpdateNode = new JSONObject(new String(message.getPayload()));
                JSONArray changedTasksArray = statusUpdateNode.getJSONArray("changed_tasks");
                for (int i = 0; i < changedTasksArray.length(); i++) {
                    JSONObject taskNode = changedTasksArray.getJSONObject(i);
                    child.addTask(taskNode.getString("id"),
                            new Timestamp(taskNode.getLong("deadline")),
                            taskNode.getString("name"),
                            taskNode.getString("description"),
                            taskNode.getInt("xp"),
                            Status.valueOf(taskNode.getString("status")));
                }
            }
        });
    }

    private void register(final String childUsername, final String parentUsername) throws MqttException, JSONException {
        client.subscribe(CHILD_CREATION_ANSWER_TOPIC + "/" + childUsername + "/" + parentUsername, 1, new IMqttMessageListener() {
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                JSONObject id = new JSONObject(new String(message.getPayload()));
                child.setId(id.get("id").toString());
                client.unsubscribe(CHILD_CREATION_ANSWER_TOPIC + "/" + childUsername + "/" + parentUsername);
            }
        });
        JSONObject registrationRequest = new JSONObject();
        registrationRequest.put("child_username", childUsername);
        registrationRequest.put("parent_username", parentUsername);
        client.publish(CHILD_CREATION_REQUEST_TOPIC, new MqttMessage(registrationRequest.toString().getBytes()));
    }

    public ChildSession getInstance(Context context) {
        if (session == null) {
            try {
                session = new ChildSession(context);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return session;
    }

    public Collection<Task> getTasks() {
        return child.getTasks();
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
        client.publish(CHILD_ACTION_TO_SERVER_TOPIC, new MqttMessage(taskUpdateNode.toString().getBytes()));
    }
}
