package es.serversurvival;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.bukkitbettermenus.BukkitBetterMenus;
import es.bukkitbettermenus.MenusDependenciesInstanceProvider;
import es.bukkitbettermenus.eventlisteners.OnInventoryClick;
import es.bukkitbettermenus.eventlisteners.OnInventoryClose;
import es.bukkitclassmapper.ClassMapperConfiguration;
import es.bukkitclassmapper._shared.utils.reflections.BukkitClassMapperInstanceProvider;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.DefaultCommandExecutorEntrypoint;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.mobs.MobMapper;
import es.bukkitclassmapper.mobs.OnPlayerInteractMob;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;
import es.dependencyinjector.DependencyInjectorBootstrapper;
import es.dependencyinjector.DependencyInjectorConfiguration;
import es.dependencyinjector.abstractions.AbstractionsRepository;
import es.dependencyinjector.abstractions.InMemoryAbstractionsRepository;
import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.InMemoryDependenciesRepository;
import es.jaime.EventBus;
import es.jaime.EventListenerDependencyProvider;
import es.jaime.ORMJava;
import es.jaime.connection.DatabaseTransactionManager;
import es.jaime.impl.EventBusSync;
import es.jaime.javaddd.domain.database.TransactionManager;
import es.serversurvival._shared.eventospixelcoins.PluginIniciado;

import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival.minecraftserver.scoreboards.ScoreboardCreator;
import es.serversurvival.minecraftserver.webaction.server.WebAcionHttpServer;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.tienda._shared.MySQLTiendaObjetoEncantamientosDeserializer;
import es.serversurvival.pixelcoins.tienda._shared.MySQLTiendaObjetoEncantamientosSerializer;
import es.serversurvival.pixelcoins.tienda._shared.TiendaItemMinecraftEncantamientos;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;


import java.util.Set;
import java.util.concurrent.Executors;

import static org.bukkit.ChatColor.*;

/**
 * 11/05/2023 -> 16747
 * 12/05/2023 -> 16246
 * 13/05/2023 -> 16145
 * 14/05/2023 -> 15974

 * 14/06/2023 -> 12931
 * 14/06/2023 -> 12641
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

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(COMMON_PACKAGE))
                .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(), new MethodAnnotationsScanner()));

        DependenciesRepository dependenciesRepository = new InMemoryDependenciesRepository();
        AbstractionsRepository abstractionsRepository = new InMemoryAbstractionsRepository();
        Set<Class<?>> excludedDependeies = Set.of(DefaultCommandExecutorEntrypoint.class, MobMapper.DefaultEntrypointPlayerInteractEntity.class,
                OnInventoryClick.class, OnInventoryClose.class);

        InstanceProviderDependencyInjector instanceProvider = new InstanceProviderDependencyInjector(dependenciesRepository, excludedDependeies);
        EventBus eventBus = new EventBusSync(COMMON_PACKAGE, instanceProvider);
        dependenciesRepository.add(EventBus.class, eventBus);
        dependenciesRepository.add(DependenciesRepository.class, dependenciesRepository);
        dependenciesRepository.add(ObjectMapper.class, new ObjectMapper());
        abstractionsRepository.add(EventBus.class, EventBusSync.class);
        abstractionsRepository.add(TransactionManager.class, DatabaseTransactionManager.class);

        ORMJava.addCustomDeserializer(TiendaItemMinecraftEncantamientos.class, new MySQLTiendaObjetoEncantamientosDeserializer());
        ORMJava.addCustomSerializer(TiendaItemMinecraftEncantamientos.class, new MySQLTiendaObjetoEncantamientosSerializer());

        DependencyInjectorBootstrapper.init(DependencyInjectorConfiguration.builder()
                .singleThreadedScan()
                .packageToScan(COMMON_PACKAGE)
                .reflections(reflections)
                .dependenciesRepository(dependenciesRepository)
                .abstractionsRepository(abstractionsRepository)
                .useDebugLogging()
                .excludedDependencies(excludedDependeies.toArray(new Class[0]))
                .customAnnotations(Command.class, Task.class, Mob.class, ScoreboardCreator.class, MySQLRepository.class)
                .excludedAbstractions(TaskRunner.class, OnPlayerInteractMob.class, CommandRunnerArgs.class, CommandRunnerNonArgs.class, UseCaseHandler.class)
                .waitUntilCompletion()
                .build());

        dependenciesRepository.get(WebAcionHttpServer.class).iniciar();

        BukkitBetterMenus.setInstanceProvider(instanceProvider);
        BukkitBetterMenus.registerEventListeners(this, Bukkit.getPluginManager());

        ClassMapperConfiguration.builder(this, COMMON_PACKAGE)
                .useDebugLogging()
                .reflections(reflections)
                .instanceProvider(instanceProvider)
                .threadPool(Executors.newCachedThreadPool())
                .commandMapper(ON_WRONG_PERMISSION, ON_WRONG_COMMAND)
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
        private final Set<Class<?>> excluded;

        @Override
        public <I, O extends I> O get(Class<I> clazz) {
            return (O) this.dependenciesRepository.get(clazz);
        }

        @Override
        public boolean isExcluded(Class<?> clazz) {
            return excluded.contains(clazz);
        }
    }
}
