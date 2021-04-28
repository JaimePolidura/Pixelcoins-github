package es.serversurvival.webconnection;

import lombok.SneakyThrows;
import org.bukkit.scheduler.BukkitRunnable;


public class RabbitMQConsumerTask extends BukkitRunnable {
    private final RabbitMQConsumer rabbitMQConsumer = new RabbitMQConsumer();

    @Override
    @SneakyThrows
    public void run() {
        rabbitMQConsumer.listenForMessages();
    }
}
