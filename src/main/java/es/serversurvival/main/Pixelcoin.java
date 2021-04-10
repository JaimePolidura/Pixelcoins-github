package es.serversurvival.main;

import es.jaimetruman.Mapper;
import es.serversurvival.mySQL.*;

import es.serversurvival.task.RabbitMQConsumerTask;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.ChatColor.*;

public final class Pixelcoin extends JavaPlugin implements AllMySQLTablesInstances{
    private static Pixelcoin plugin;

    public static Pixelcoin getInstance() {
        return plugin;
    }

    public void onEnable() {
        plugin = this;

        MySQL.conectar();
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
