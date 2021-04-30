package es.serversurvival;

import es.jaime.Event;
import es.jaime.EventBus;
import es.jaime.impl.EventBusSynch;
import es.jaimetruman.Mapper;
import es.jaimetruman.task.BukkitTimeUnit;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.shared.mysql.MySQL;
import es.serversurvival.webconnection.RabbitMQConsumerTask;
import es.serversurvival.shared.scoreboards.ScoreBoardManager;
import es.serversurvival.shared.scoreboards.ScoreboardUpdateTask;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


import static org.bukkit.ChatColor.*;

public final class Pixelcoin extends JavaPlugin implements AllMySQLTablesInstances {
    private static Pixelcoin plugin;
    private ScoreBoardManager scoreBoardManager;
    private EventBus eventBus;
    private ScoreboardUpdateTask updater;

    public Pixelcoin () {
        plugin = this;
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

    public static ScoreboardUpdateTask scoreboardUpdater() {
        return plugin.updater;
    }

    @Override
    public void onEnable() {
        MySQL.conectar();

        this.scoreBoardManager = new ScoreBoardManager();
        this.eventBus = new EventBusSynch("es.serversurvival");

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
        this.updater = new ScoreboardUpdateTask();
        updater.runTaskTimer(this, BukkitTimeUnit.MINUTE, BukkitTimeUnit.MINUTE);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, updater, BukkitTimeUnit.MINUTE, BukkitTimeUnit.MINUTE);
    }

    private void setUpCommandsMobListenersTask() {
        String onWrongCommand = DARK_RED + "Comando no encontrado /ayuda";
        String onWrongSender = DARK_RED + "Necesitas estar en el servidor para ejecutar el comando";

        Mapper.build(this)
                .all(onWrongCommand, onWrongSender)
                .startScanning();
    }
}
