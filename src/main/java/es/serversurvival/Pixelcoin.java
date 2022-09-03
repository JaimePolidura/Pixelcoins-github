package es.serversurvival;

import es.bukkitclassmapper.Mapper;
import es.bukkitclassmapper._shared.utils.ClassMapperInstanceProvider;
import es.bukkitclassmapper._shared.utils.InstanceProvider;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.menus.MenuService;
import es.bukkitclassmapper.menus.modules.sync.SyncMenuService;
import es.bukkitclassmapper.mobs.Mob;
import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.dependencyinjector.DependencyInjectorBootstrapper;
import es.dependencyinjector.DependencyInjectorConfiguration;
import es.dependencyinjector.repository.DependenciesRepository;
import es.dependencyinjector.repository.InMemoryDependenciesRepository;
import es.jaime.Event;
import es.jaime.EventBus;
import es.jaime.impl.EventBusSynch;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.eventospixelcoins.PluginIniciado;
import es.serversurvival._shared.mysql.MySQLConfiguration;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfoRepository;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.acciones.AccionesApiService;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.criptomonedas.CriptomonedasApiService;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.materiasprimas.MateriasPrimasApiService;
import es.serversurvival.bolsa.activosinfo._shared.infrastructure.MySQLActivoInfoRepository;
import es.serversurvival.bolsa.activosinfo._shared.infrastructure.tipoactivos.AccionesApiServiceIEXCloud;
import es.serversurvival.bolsa.activosinfo._shared.infrastructure.tipoactivos.CriptomonedasApiServiceIEXCloud;
import es.serversurvival.bolsa.activosinfo._shared.infrastructure.tipoactivos.MateriasPrimasApiServiceIEXCloud;
import es.serversurvival.bolsa.ordenespremarket._shared.application.OrdenesPremarketService;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.OrderesPremarketRepository;
import es.serversurvival.bolsa.ordenespremarket._shared.infrastructure.MySQLOrdenesPremarketRepository;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionesAbiertasRepository;
import es.serversurvival.bolsa.posicionesabiertas._shared.infrastructure.MySQLPosicionesAbiertasRepository;
import es.serversurvival.bolsa.posicionescerradas._shared.application.PosicionesCerradasService;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.PosicionesCerradasRepository;
import es.serversurvival.bolsa.posicionescerradas._shared.infrastructure.MySQLPosicionesCerradasRepository;
import es.serversurvival.empresas.accionistasserver._shared.application.AccionistasServerService;
import es.serversurvival.empresas.accionistasserver._shared.domain.AccionistasServerRepository;
import es.serversurvival.empresas.accionistasserver._shared.infrastructure.MySQLAccionistasServerRepository;
import es.serversurvival.empresas.empleados._shared.infrastructure.MySQLEmpleadosRepository;
import es.serversurvival.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertasAccionesServerRepository;
import es.serversurvival.empresas.ofertasaccionesserver._shared.infrastructure.MySQLOfertasAccionesServerRepository;
import es.serversurvival.mensajes._shared.infrastructure.MySQLMensajesRepository;
import es.serversurvival.tienda._shared.infrastructure.MySQLTiendaRepository;
import es.serversurvival.transacciones._shared.infrastructure.MySQLTransaccionesRepository;
import es.serversurvival.web.cuentasweb._shared.application.CuentasWebService;
import es.serversurvival.web.cuentasweb._shared.domain.CuentasWebRepository;
import es.serversurvival.web.cuentasweb._shared.infrastructure.MySQLCuentasWebRepository;
import es.serversurvival.deudas._shared.application.DeudasService;
import es.serversurvival.deudas._shared.domain.DeudasRepository;
import es.serversurvival.deudas._shared.infrastructure.MySQLDeudasRepository;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.EmpleadosRepository;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.EmpresasRepostiory;
import es.serversurvival.empresas.empresas._shared.infrastructure.MySQLEmpresasRepository;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.JugadoresRepository;
import es.serversurvival.jugadores._shared.infrastructure.MySQLJugadoresRepository;
import es.serversurvival.mensajes._shared.application.MensajesService;
import es.serversurvival.mensajes._shared.domain.MensajesRepository;
import es.serversurvival.tienda._shared.application.TiendaService;
import es.serversurvival.tienda._shared.domain.TiendaRepository;
import es.serversurvival.transacciones._shared.application.TransaccionesService;
import es.serversurvival.transacciones._shared.domain.TransaccionesRepository;
import es.serversurvival.web.verificacioncuentas._shared.application.VerificacionCuentaService;
import es.serversurvival.web.verificacioncuentas._shared.domain.VerificacionCuentaRepository;
import es.serversurvival.web.verificacioncuentas._shared.infrastructure.InMemoryVerificacionCuentaRepository;
import es.serversurvival._shared.scoreboards.ScoreBoardManager;
import es.serversurvival._shared.scoreboards.ScoreboardUpdateTask;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;

import static org.bukkit.ChatColor.*;

public final class Pixelcoin extends JavaPlugin {
    private static final String ON_WRONG_COMMAND = DARK_RED + "Comando no encontrado /ayuda";
    private static final String ON_WRONG_PERMISSION = DARK_RED + "Tienes que ser administrador para ejecutar ese comando";

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
        DependenciesRepository dependenciesRepository = new InMemoryDependenciesRepository();

        this.eventBus = new EventBusSynch("es.serversurvival");

        DependencyInjectorBootstrapper.init(DependencyInjectorConfiguration.builder()
                .packageToScan("es.serversurvival")
                .dependenciesRepository(dependenciesRepository)
                .customAnnotations(Command.class, Task.class, Mob.class)
                .build());
        Mapper.build(this)
                .all(ON_WRONG_COMMAND, ON_WRONG_PERMISSION)
                .instanceProvider(InstanceProviderDependencyInjector.fromRepository(dependenciesRepository))
                .startScanning();

        this.loadAllDependenciesContainer();
        this.setUpCommandsMobListenersTask();
        this.setUpScoreboardUpdater();

        getServer().getConsoleSender().sendMessage(GREEN + "------------------------------");

        this.eventBus.publish(new PluginIniciado());
    }

    private void setUpScoreboardUpdater () {
        this.updater = new ScoreboardUpdateTask();
        updater.runTaskTimer(this, BukkitTimeUnit.MINUTE, BukkitTimeUnit.MINUTE);
    }

    private void setUpCommandsMobListenersTask() {
    }

    private void loadAllDependenciesContainer() {
        var mysqlCOnfiguration = new MySQLConfiguration();

        DependecyContainer.addAll(new HashMap<>() {{
            put(MySQLConfiguration.class, mysqlCOnfiguration);
            put(EventBus.class, eventBus);
            put(ExecutorService.class, Funciones.POOL);
            put(MenuService.class, ClassMapperInstanceProvider.MENU_SERVICE);
            put(SyncMenuService.class, ClassMapperInstanceProvider.SYNC_MENU_SERVICE);
        }});

        DependecyContainer.addAll(new HashMap<>(){{
            put(JugadoresRepository.class, new MySQLJugadoresRepository(mysqlCOnfiguration));
            put(MensajesRepository.class, new MySQLMensajesRepository(mysqlCOnfiguration));
            put(TransaccionesRepository.class, new MySQLTransaccionesRepository(mysqlCOnfiguration));
            put(DeudasRepository.class, new MySQLDeudasRepository(mysqlCOnfiguration));
            put(TiendaRepository.class, new MySQLTiendaRepository(mysqlCOnfiguration));
            put(EmpresasRepostiory.class, new MySQLEmpresasRepository(mysqlCOnfiguration));
            put(EmpleadosRepository.class, new MySQLEmpleadosRepository(mysqlCOnfiguration));
            put(CuentasWebRepository.class, new MySQLCuentasWebRepository(mysqlCOnfiguration));
            put(VerificacionCuentaRepository.class, new InMemoryVerificacionCuentaRepository());
            put(OrderesPremarketRepository.class, new MySQLOrdenesPremarketRepository(mysqlCOnfiguration));
            put(PosicionesAbiertasRepository.class, new MySQLPosicionesAbiertasRepository(mysqlCOnfiguration));
            put(PosicionesCerradasRepository.class, new MySQLPosicionesCerradasRepository(mysqlCOnfiguration));
            put(ActivoInfoRepository.class, new MySQLActivoInfoRepository(mysqlCOnfiguration));
            put(OfertasAccionesServerRepository.class, new MySQLOfertasAccionesServerRepository(mysqlCOnfiguration));
            put(AccionistasServerRepository.class, new MySQLAccionistasServerRepository(mysqlCOnfiguration));
        }});

        DependecyContainer.addAll(new HashMap<>(){{
            put(JugadoresService.class, new JugadoresService(DependecyContainer.get(JugadoresRepository.class)));
            put(MensajesService.class, new MensajesService());
            put(TransaccionesService.class, new TransaccionesService());
            put(DeudasService.class, new DeudasService());
            put(TiendaService.class, new TiendaService());
            put(EmpresasService.class, new EmpresasService());
            put(EmpleadosService.class, new EmpleadosService());
            put(CuentasWebService.class, new CuentasWebService());
            put(VerificacionCuentaService.class, new VerificacionCuentaService());
            put(OrdenesPremarketService.class, new OrdenesPremarketService());
            put(PosicionesAbiertasSerivce.class, new PosicionesAbiertasSerivce());
            put(PosicionesCerradasService.class, new PosicionesCerradasService());
            put(OfertasAccionesServerService.class, new OfertasAccionesServerService());
            put(AccionistasServerService.class, new AccionistasServerService());
            put(AccionesApiService.class, new AccionesApiServiceIEXCloud());
            put(MateriasPrimasApiService.class, new MateriasPrimasApiServiceIEXCloud());
            put(CriptomonedasApiService.class, new CriptomonedasApiServiceIEXCloud());
        }});
    }

    @AllArgsConstructor
    private static class InstanceProviderDependencyInjector implements InstanceProvider {
        private final DependenciesRepository dependenciesRepository;

        public static InstanceProviderDependencyInjector fromRepository(DependenciesRepository dependenciesRepository) {
            return new InstanceProviderDependencyInjector(dependenciesRepository);
        }

        @Override
        public <I, O extends I> O get(Class<I> aClass) {
            return (O) this.dependenciesRepository.get(aClass);
        }
    }
}
