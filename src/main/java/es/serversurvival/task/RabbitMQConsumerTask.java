package es.serversurvival.task;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import es.serversurvival.mySQL.ConversacionesWeb;
import es.serversurvival.socketWeb.RabbitMQConsumer;
import es.serversurvival.socketWeb.ServerSocketWeb;
import es.serversurvival.socketWeb.SocketMessagge;
import es.serversurvival.socketWeb.messagges.*;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RabbitMQConsumerTask extends BukkitRunnable {
    private RabbitMQConsumer rabbitMQConsumer = new RabbitMQConsumer();

    @Override
    public void run() {
        try {
            rabbitMQConsumer.listenForMessages();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
