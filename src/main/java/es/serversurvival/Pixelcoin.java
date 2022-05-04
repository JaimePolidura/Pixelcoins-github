package es.serversurvival;

import es.jaime.Event;
import es.jaime.EventBus;
import es.jaime.impl.EventBusSynch;
import es.jaimetruman.Mapper;
import es.jaimetruman.task.BukkitTimeUnit;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival._shared.mysql.newformat.MySQLConfiguration;
import es.serversurvival.jugadores._shared.newformat.application.JugadoresService;
import es.serversurvival.jugadores._shared.newformat.domain.JugadoresRepository;
import es.serversurvival.jugadores._shared.newformat.infrastructure.MySQLJugadoresRepository;
import es.serversurvival.mensajes._shared.application.MensajesService;
import es.serversurvival.mensajes._shared.domain.MensajesRepository;
import es.serversurvival.webconnection.RabbitMQConsumerTask;
import es.serversurvival._shared.scoreboards.ScoreBoardManager;
import es.serversurvival._shared.scoreboards.ScoreboardUpdateTask;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

import static org.bukkit.ChatColor.*;

public final class Pixelcoin extends JavaPlugin implements AllMySQLTablesInstances {
    private static Pixelcoin plugin;
    private ScoreBoardManager scoreBoardManager;
    private EventBus eventBus;
    private ScoreboardUpdateTask updater;

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
        plugin = this;

        MySQLRepository.conectar();

        this.scoreBoardManager = new ScoreBoardManager();
        this.eventBus = new EventBusSynch("es.serversurvival");

        conversacionesWebMySQL.borrarTodasConversacionesWeb();

        getLogger().info("------------Plugin activado -------------");
        getServer().getConsoleSender().sendMessage(GREEN + "------------------------------");

        this.setUpCommandsMobListenersTask();
        this.setUpRabbitMQConsumer();
        this.setUpScoreboardUpdater();
        this.loadAllDependenciesContainer();

        getServer().getConsoleSender().sendMessage(GREEN + "------------------------------");
    }

    private void setUpRabbitMQConsumer () {
        RabbitMQConsumerTask rabbitMQConsumerTask = new RabbitMQConsumerTask();
        rabbitMQConsumerTask.runTaskAsynchronously(this);
    }

    private void setUpScoreboardUpdater () {
        this.updater = new ScoreboardUpdateTask();
        updater.runTaskTimer(this, BukkitTimeUnit.MINUTE, BukkitTimeUnit.MINUTE);
    }

    private void setUpCommandsMobListenersTask() {
        String onWrongCommand = DARK_RED + "Comando no encontrado /ayuda";
        String onWrongPermissions = DARK_RED + "Tienes que ser administrador para ejecutar ese comando";

        Mapper.build(this)
                .all(onWrongCommand, onWrongPermissions)
                .startScanning();
    }

    private void loadAllDependenciesContainer() {
        var mysqlCOnfiguration = new MySQLConfiguration();

        DependecyContainer.addAll(new HashMap<>() {{
            put(MySQLConfiguration.class, mysqlCOnfiguration);
        }});

        DependecyContainer.addAll(new HashMap<>(){{
            put(JugadoresRepository.class, new MySQLJugadoresRepository(DependecyContainer.get(MySQLConfiguration.class)));
            put(MensajesRepository.class, new MySQLJugadoresRepository(DependecyContainer.get(MySQLConfiguration.class)));
        }});

        DependecyContainer.addAll(new HashMap<>(){{
            put(JugadoresService.class, new JugadoresService());
            put(MensajesService.class, new MensajesService());
        }});
    }
}
