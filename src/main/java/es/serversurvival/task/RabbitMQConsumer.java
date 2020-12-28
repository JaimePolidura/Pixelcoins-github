package es.serversurvival.task;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.impl.AMQImpl;
import es.serversurvival.socketWeb.SocketMessagge;
import es.serversurvival.webchat.WebChatManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RabbitMQConsumer extends BukkitRunnable {
    private final String QUEUE_NAME = "chat";
    private final ConnectionFactory factory;
    private WebChatManager chatManager;

    public RabbitMQConsumer () {
        this.factory = new ConnectionFactory();
        this.chatManager = new WebChatManager();
    }

    @Override
    public void run() {
        try {
            System.out.println("0");

            listenForMessages();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listenForMessages() throws IOException, TimeoutException {
        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            while (true) {
                channel.basicConsume(QUEUE_NAME, true, (tag, rawMessage) -> {
                    processRawMessage(rawMessage.getBody());
                }, this::onFailureRecievedMessage);
            }
        }
    }

    private void processRawMessage (byte[] rawMessage) {
        String rawStringMessage = new String(rawMessage, StandardCharsets.UTF_8);
        SocketMessagge processedMessage = new SocketMessagge(rawStringMessage);

        chatManager.handlerMessageFromServer(processedMessage);
    }

    private void onFailureRecievedMessage (String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "ERROR RABBIT MQ: " + message);
    }
}
