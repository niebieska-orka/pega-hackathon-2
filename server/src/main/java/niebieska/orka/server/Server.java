package niebieska.orka.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;

public class Server {
    private static final String BROKER = "tcp://192.168.43.126:1883";
    private static final String TOPIC_PREFIX = "niebieska/orka/health/hero";
    private static final String CHILD_STATUS_REQUEST_TOPIC = TOPIC_PREFIX + "/status/request";
    private static final String CHILD_STATUS_UPDATE_TOPIC = TOPIC_PREFIX + "/status/update";
    private static final String CHILD_ACTION_TO_SERVER_TOPIC = TOPIC_PREFIX + "/action/child/server";
    private static final String CHILD_ACTION_TO_PARENT_TOPIC = TOPIC_PREFIX + "/action/child/parent";
    private static final String PARENT_ACTION_TO_SERVER_TOPIC = TOPIC_PREFIX + "/action/parent/server";

    //when sending, add "/{ID}" to topic
    public static void main(String... args) throws Exception {
        MqttClient client = new MqttClient(BROKER, "niebieska.orka.health.hero.server", new MemoryPersistence());
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        System.out.println("Connecting to broker: " + BROKER);
        client.connect(connOpts);
        System.out.println("Connected");
        client.subscribe(CHILD_STATUS_REQUEST_TOPIC, 1, Server::processChildStatusRequest);
        client.subscribe(CHILD_ACTION_TO_SERVER_TOPIC, 1, Server::processChildAction);
        client.subscribe(PARENT_ACTION_TO_SERVER_TOPIC, 1, Server::processParentAction);
        while (true) {
            System.out.println("Idling...");
            Thread.sleep(5000);
        }
    }

    private static void processChildStatusRequest(String topic, MqttMessage message) throws IOException {
        final ObjectNode node = new ObjectMapper().readValue(message.getPayload(), ObjectNode.class);
        System.out.println(node.get("id"));
    }

    private static void processChildAction(String topic, MqttMessage message) {
        System.out.println(topic + ": " + message);
    }

    private static void processParentAction(String topic, MqttMessage message) {
        System.out.println(topic + ": " + message);
    }
}
