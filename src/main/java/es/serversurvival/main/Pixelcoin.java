package es.serversurvival.main;

import es.jaime.Event;
import es.jaime.EventBus;
import es.jaime.EventBusAsynch;
import es.jaimetruman.Mapper;
import es.serversurvival.mySQL.*;

import es.serversurvival.task.RabbitMQConsumerTask;
import es.serversurvival.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.block.data.Bisected;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

import static org.bukkit.ChatColor.*;

public final class Pixelcoin extends JavaPlugin implements AllMySQLTablesInstances{
    private static Pixelcoin plugin;
    private EventBus eventBus;

    public static Pixelcoin getInstance() {
        return plugin;
    }

    public static void publish (Event event) {
        plugin.eventBus.publish(event);
    }

    @Override
    public void onEnable() {
        MySQL.conectar();

        plugin = this;

        this.eventBus = new EventBusAsynch(Funciones.POOL, "es.serversurvival");

        conversacionesWebMySQL.borrarTodasConversacionesWeb();

        getLogger().info("------------Plugin activado -------------");
        getServer().getConsoleSender().sendMessage(GREEN + "------------------------------");

        this.setUpCommandsMobListenersTask();
        this.setUpRabbitMQConsumer();

        getServer().getConsoleSender().sendMessage(GREEN + "------------------------------");
    }

    private void setUpRabbitMQConsumer () {
        RabbitMQConsumerTask rabbitMQConsumerTask = new RabbitMQConsumerTask();
        rabbitMQConsumerTask.runTaskAsynchronously(this);
    }

    private void setUpCommandsMobListenersTask() {
        String onWrongCommand = DARK_RED + "Comando no encontrado /ayuda";
        String onWrongSender = DARK_RED + "Necesitas estar en el servidor para ejecutar el comando";

        Mapper.build("es.serversurvival", this)
                .commandMapper(onWrongCommand, onWrongSender)
                .mobMapper()
                .eventListenerMapper()
                .taskMapper()
                .startScanning();
    }
}
