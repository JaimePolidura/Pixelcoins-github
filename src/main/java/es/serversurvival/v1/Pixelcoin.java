package es.serversurvival.v1;

import es.bukkitbettermenus.BetterMenusInstanceProvider;
import es.bukkitbettermenus.MenusDependenciesInstanceProvider;
import es.bukkitclassmapper.ClassMapperConfiguration;
import es.bukkitclassmapper._shared.utils.reflections.BukkitClassMapperInstanceProvider;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.task.Task;
import es.dependencyinjector.DependencyInjectorBootstrapper;
import es.dependencyinjector.DependencyInjectorConfiguration;
import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.InMemoryDependenciesRepository;
import es.jaime.EventBus;
import es.jaime.EventListenerDependencyProvider;
import es.jaime.impl.EventBusSync;
import es.serversurvival.v1._shared.eventospixelcoins.PluginIniciado;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;


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

    @SneakyThrows
    @Override
    public void onEnable() {
        getLogger().info("------------Iniciando Pixelcoins -------------");
        getServer().getConsoleSender().sendMessage(GREEN + "------------------------------");

        DependenciesRepository dependenciesRepository = new InMemoryDependenciesRepository();

        InstanceProviderDependencyInjector instanceProvider = new InstanceProviderDependencyInjector(dependenciesRepository);
        EventBus eventBus = new EventBusSync("es.serversurvival", instanceProvider);
        dependenciesRepository.add(EventBus.class, eventBus);

        DependencyInjectorBootstrapper.init(DependencyInjectorConfiguration.builder()
                .packageToScan(COMMON_PACKAGE)
                .dependenciesRepository(dependenciesRepository)
                .customAnnotations(Command.class, Task.class, Mob.class)
                .waitUntilCompletion()
                .build());

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

        eventBus.publish(new PluginIniciado());
    }

    @AllArgsConstructor
    private static class InstanceProviderDependencyInjector implements BukkitClassMapperInstanceProvider, MenusDependenciesInstanceProvider, EventListenerDependencyProvider {
        private final DependenciesRepository dependenciesRepository;

        @Override
        public <I, O extends I> O get(Class<I> clazz) {
            return (O) this.dependenciesRepository.get(clazz);
        }
    }
}
