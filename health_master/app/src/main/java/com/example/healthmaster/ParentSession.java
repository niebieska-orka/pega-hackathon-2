package com.example.healthmaster;

import android.content.Context;

import com.example.healthmaster.model.Status;
import com.example.healthmaster.model.Task;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.List;

public class ParentSession {
    private static final String BROKER = "tcp://192.168.43.126:1883";
    private static final String TOPIC_PREFIX = "niebieska/orka/health/hero";
    private static final String PARENT_STATUS_REQUEST_TOPIC = TOPIC_PREFIX + "/parent/status/request";
    private static final String PARENT_STATUS_UPDATE_TOPIC = TOPIC_PREFIX + "/parent/status/update";
    private static final String PARENT_ACTION_TO_SERVER_TOPIC = TOPIC_PREFIX + "/action/parent/server";
    private static ParentSession parentSession;
    private final Context context;
    private final MqttAndroidClient client;

    private final String id;
    private List<Task> childTasks;
    private List<Task> exampleTasks;

    private ParentSession(Context context) throws MqttException {
        this.context = context;
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(context, BROKER, clientId, new MemoryPersistence(), MqttAndroidClient.Ack.AUTO_ACK);
        id = "2";
        client.connect(context, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                try {
                    Thread.sleep(800);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                throw new RuntimeException(exception);
            }
        });
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

    public void confirm(Task task, boolean accepted) {
        if (accepted)
            task.setStatus(Status.COMPLETED);
        else
            task.setStatus(Status.FAILED);
        // TO DO
        // SEND TO SERVER
    }

    public void addTask(Task task) {
        // TO DO
        // SEND TO SERVER
    }

    public void logIn(String parentUsername) {
        //GET DATA FROM SERVER
    }

    public void register(String childUsername, String parentUsername) {
        //REGISTER AND GET DATA FROM SERVER
    }
}
