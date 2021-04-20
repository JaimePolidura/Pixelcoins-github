package es.serversurvival.main;

import es.jaime.Event;
import es.jaime.EventBus;
import es.jaime.impl.EventBusSynch;
import es.jaimetruman.Mapper;
import es.jaimetruman.task.BukkitTimeUnit;
import es.serversurvival.mySQL.*;

import es.serversurvival.task.RabbitMQConsumerTask;
import es.serversurvival.task.ScoreBoardManager;
import es.serversurvival.task.ScoreboardUpdater;
import org.bukkit.plugin.java.JavaPlugin;


import static org.bukkit.ChatColor.*;

public final class Pixelcoin extends JavaPlugin implements AllMySQLTablesInstances{
    private static Pixelcoin plugin;
    private final ScoreBoardManager scoreBoardManager;
    private final EventBus eventBus;
    private ScoreboardUpdater updater;

    public Pixelcoin () {
        plugin = this;

        this.scoreBoardManager = new ScoreBoardManager();
        this.eventBus = new EventBusSynch("es.serversurvival");
    }

    public static Pixelcoin getInstance() {
        return plugin;
    }

    public static void publish (Event event) {
        plugin.eventBus.publish(event);
    }

    public static ScoreBoardManager scoreboarManager () {
        return plugin.scoreBoardManager;
    }

    public static ScoreboardUpdater scoreboardUpdater() {
        return plugin.updater;
    }

    @Override
    public void onEnable() {
        MySQL.conectar();

        conversacionesWebMySQL.borrarTodasConversacionesWeb();

        getLogger().info("------------Plugin activado -------------");
        getServer().getConsoleSender().sendMessage(GREEN + "------------------------------");

        this.setUpCommandsMobListenersTask();
        this.setUpRabbitMQConsumer();
        this.setUpScoreboardUpdater();

        getServer().getConsoleSender().sendMessage(GREEN + "------------------------------");
    }

    private void setUpRabbitMQConsumer () {
        RabbitMQConsumerTask rabbitMQConsumerTask = new RabbitMQConsumerTask();
        rabbitMQConsumerTask.runTaskAsynchronously(this);
    }

    private void setUpScoreboardUpdater () {
        ScoreboardUpdater updater = new ScoreboardUpdater();
        updater.runTaskTimer(this, BukkitTimeUnit.MINUTE, BukkitTimeUnit.MINUTE);

        this.updater = updater;
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
