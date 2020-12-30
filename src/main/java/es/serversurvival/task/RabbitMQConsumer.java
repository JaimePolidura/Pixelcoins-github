package es.serversurvival.task;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import es.serversurvival.mySQL.ConversacionesWeb;
import es.serversurvival.socketWeb.SocketMessagge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RabbitMQConsumer extends BukkitRunnable {
    private final String QUEUE_NAME = "chat";
    private Connection connection;

    public RabbitMQConsumer () {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            this.connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            listenForMessages();
        } catch (Exception e) {
            e.printStackTrace();
            run();
        }
    }

    private void listenForMessages() throws IOException, TimeoutException {
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        Bukkit.getConsoleSender().sendMessage("[Pixelcoins] RabbitMQ consumer running...");

        channel.basicConsume(QUEUE_NAME, true, (tag, rawMessage) -> processRawMessage(rawMessage.getBody()), this::onFailureRecievedMessage);
    }

    private void processRawMessage (byte[] rawMessage) {
        String rawStringMessage = new String(rawMessage, StandardCharsets.UTF_8);
        SocketMessagge processedMessage = new SocketMessagge(rawStringMessage);
        
        ConversacionesWeb.conectar();
        if(processedMessage.getName().equalsIgnoreCase("chat")){
            ConversacionesWeb.INSTANCE.handleMensajeWeb(processedMessage);
        }else{
            ConversacionesWeb.INSTANCE.handleDesconexionWeb(processedMessage);
        }

        ConversacionesWeb.desconectar();
    }

    private void onFailureRecievedMessage (String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "ERROR RABBIT MQ: " + message);
    }
}
