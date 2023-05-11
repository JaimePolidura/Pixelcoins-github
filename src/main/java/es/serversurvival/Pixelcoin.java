package es.serversurvival;

import es.bukkitbettermenus.BetterMenusInstanceProvider;
import es.bukkitbettermenus.MenusDependenciesInstanceProvider;
import es.bukkitclassmapper.ClassMapperConfiguration;
import es.bukkitclassmapper._shared.utils.reflections.InstanceProvider;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.dependencyinjector.DependencyInjectorBootstrapper;
import es.dependencyinjector.DependencyInjectorConfiguration;
import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.InMemoryDependenciesRepository;
import es.jaime.Event;
import es.jaime.EventBus;
import es.jaime.impl.EventBusSynch;
import es.serversurvival._shared.eventospixelcoins.PluginIniciado;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival._shared.scoreboards.ScoreBoardManager;
import es.serversurvival._shared.scoreboards.ScoreboardUpdateTask;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;

import static org.bukkit.ChatColor.*;

/**
 * 11/05/2023 -> 16747
 */
public final class Pixelcoin extends JavaPlugin {
    private static final String ON_WRONG_COMMAND = DARK_RED + "Comando no encontrado /ayuda";
    private static final String ON_WRONG_PERMISSION = DARK_RED + "Tienes que ser administrador para ejecutar ese comando";
    private static final String COMMON_PACKAGE = "es.serversurvival";

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

    @SneakyThrows
    @Override
    public void onEnable() {
        plugin = this;

        this.scoreBoardManager = new ScoreBoardManager();

        getLogger().info("------------Plugin activado -------------");
        getServer().getConsoleSender().sendMessage(GREEN + "------------------------------");

        this.eventBus = new EventBusSynch("es.serversurvival");

        DependenciesRepository dependenciesRepository = new InMemoryDependenciesRepository();

        DependencyInjectorBootstrapper.init(DependencyInjectorConfiguration.builder()
                .packageToScan(COMMON_PACKAGE)
                .dependenciesRepository(dependenciesRepository)
                .customAnnotations(Command.class, Task.class, Mob.class)
                .waitUntilCompletion()
                .build());
        InstanceProviderDependencyInjector instanceProvider = new InstanceProviderDependencyInjector(dependenciesRepository);

        BetterMenusInstanceProvider.setInstanceProvider(instanceProvider);

        ClassMapperConfiguration.builder(this, COMMON_PACKAGE)
                    .instanceProvider(instanceProvider)
                    .commandMapper(ON_WRONG_COMMAND, ON_WRONG_PERMISSION)
                    .taskMapper()
                    .mobMapper()
                    .eventListenerMapper()
                    .waitUntilCompletion()
                    .build()
                .startScanning();

        dependenciesRepository.add(ExecutorService.class, Funciones.POOL);

        this.setUpScoreboardUpdater();

        getServer().getConsoleSender().sendMessage(GREEN + "------------------------------");

        this.eventBus.publish(new PluginIniciado());
    }

    private void setUpScoreboardUpdater () {
        this.updater = new ScoreboardUpdateTask();
        updater.runTaskTimer(this, BukkitTimeUnit.MINUTE, BukkitTimeUnit.MINUTE);
    }

    @AllArgsConstructor
    private static class InstanceProviderDependencyInjector implements InstanceProvider, MenusDependenciesInstanceProvider {
        private final DependenciesRepository dependenciesRepository;

        @Override
        public <I, O extends I> O get(Class<I> clazz) {
            return (O) this.dependenciesRepository.get(clazz);
        }
    }
}
