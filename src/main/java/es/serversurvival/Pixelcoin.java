package es.serversurvival;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.bukkitbettermenus.BetterMenusInstanceProvider;
import es.bukkitbettermenus.MenusDependenciesInstanceProvider;
import es.bukkitclassmapper.ClassMapperConfiguration;
import es.bukkitclassmapper._shared.utils.reflections.BukkitClassMapperInstanceProvider;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunner;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;
import es.dependencyinjector.DependencyInjectorBootstrapper;
import es.dependencyinjector.DependencyInjectorConfiguration;
import es.dependencyinjector.abstractions.AbstractionsRepository;
import es.dependencyinjector.abstractions.InMemoryAbstractionsRepository;
import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.InMemoryDependenciesRepository;
import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventBus;
import es.jaime.EventListenerDependencyProvider;
import es.jaime.impl.EventBusSync;
import es.serversurvival._shared.eventospixelcoins.PluginIniciado;

import es.serversurvival.minecraftserver.scoreboards.ScoreboardCreator;
import es.serversurvival.minecraftserver.webaction.server.WebAcionHttpServer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.Set;

import static org.bukkit.ChatColor.*;

/**
 * 11/05/2023 -> 16747
 * 12/05/2023 -> 16246
 * 13/05/2023 -> 16145
 * 14/05/2023 -> 15974
 */
public final class Pixelcoin extends JavaPlugin {
    private static final String ON_WRONG_COMMAND = DARK_RED + "Comando no encontrado /ayuda";
    private static final String ON_WRONG_PERMISSION = DARK_RED + "Tienes que ser administrador para ejecutar ese comando";
    private static final String COMMON_PACKAGE = "es.serversurvival";

    private static DependenciesRepository DEPENDENCIES_REPOSTIORY;

    @SneakyThrows
    @Override
    public void onEnable() {
        getLogger().info("------------Iniciando Pixelcoins -------------");
        getServer().getConsoleSender().sendMessage(GREEN + "------------------------------");

        DependenciesRepository dependenciesRepository = new InMemoryDependenciesRepository();
        AbstractionsRepository abstractionsRepository = new InMemoryAbstractionsRepository();

        InstanceProviderDependencyInjector instanceProvider = new InstanceProviderDependencyInjector(dependenciesRepository);
        EventBus eventBus = new EventBusSync("es.serversurvival", instanceProvider);
        dependenciesRepository.add(EventBus.class, eventBus);
        dependenciesRepository.add(ObjectMapper.class, new ObjectMapper());
        abstractionsRepository.add(EventBus.class, EventBusSync.class);

        DependencyInjectorBootstrapper.init(DependencyInjectorConfiguration.builder()
                .packageToScan(COMMON_PACKAGE)
                .dependenciesRepository(dependenciesRepository)
                .abstractionsRepository(abstractionsRepository)
                .customAnnotations(Command.class, Task.class, Mob.class, ScoreboardCreator.class)
                .logging(Bukkit.getLogger())
                .singleThreadedScan()
                .excludedAbstractions(TaskRunner.class, OnPlayerInteractMob.class, CommandRunnerArgs.class, CommandRunnerNonArgs.class)
                .waitUntilCompletion()
                .build());

        dependenciesRepository.get(WebAcionHttpServer.class).iniciar();

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

        getServer().getConsoleSender().sendMessage(GREEN + "------------------------------");

        DEPENDENCIES_REPOSTIORY = dependenciesRepository;

        eventBus.publish(new PluginIniciado());
    }

    @Override
    public void onDisable() {
        DEPENDENCIES_REPOSTIORY.get(WebAcionHttpServer.class)
                .stop();
    }

    @AllArgsConstructor
    private static class InstanceProviderDependencyInjector implements
            BukkitClassMapperInstanceProvider,
            MenusDependenciesInstanceProvider,
            EventListenerDependencyProvider {

        private final DependenciesRepository dependenciesRepository;

        @Override
        public <I, O extends I> O get(Class<I> clazz) {
            return (O) this.dependenciesRepository.get(clazz);
        }
    }
}
