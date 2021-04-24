package es.serversurvival.legacy.task;

import es.serversurvival.legacy.socketWeb.RabbitMQConsumer;
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
