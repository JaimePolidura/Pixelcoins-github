package es.serversurvival;

import es.jaime.Event;
import es.jaime.EventBus;
import es.jaime.impl.EventBusSynch;
import es.jaimetruman.Mapper;
import es.jaimetruman.task.BukkitTimeUnit;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival._shared.mysql.newformat.MySQLConfiguration;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivoInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfoCacheRepository;
import es.serversurvival.bolsa.activosinfo._shared.infrastructure.InMemoryActivoInfoCacheRepository;
import es.serversurvival.bolsa.ordenespremarket._shared.application.OrdenesPremarketService;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.OrderesPremarketRepository;
import es.serversurvival.bolsa.ordenespremarket._shared.infrastructure.MySQLOrdenesPremarketRepository;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionesAbiertasRepository;
import es.serversurvival.bolsa.posicionesabiertas._shared.infrastructure.MySQLPosicionesAbiertasRepository;
import es.serversurvival.bolsa.posicionescerradas._shared.application.PosicionesCerradasService;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.PosicionesCerradasRepository;
import es.serversurvival.bolsa.posicionescerradas._shared.infrastructure.MySQLPosicionesCerradasRepository;
import es.serversurvival.empresas.accionistasempresasserver._shared.application.AccionistasEmpresasServerService;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.AccionistasEmpresasServerRepository;
import es.serversurvival.empresas.accionistasempresasserver._shared.infrastructure.MySQLAccionistasEmpresasServerRepository;
import es.serversurvival.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertasAccionesServerRepository;
import es.serversurvival.empresas.ofertasaccionesserver._shared.infrastructure.MySQLOfertasAccionesServerRepository;
import es.serversurvival.web.conversacionesweb._shared.application.ConversacionesWebService;
import es.serversurvival.web.conversacionesweb._shared.domain.ConversacionesWebRepostiory;
import es.serversurvival.web.conversacionesweb._shared.infrastructure.InMemoryConversacionesRepository;
import es.serversurvival.web.cuentasweb._shared.application.CuentasWebService;
import es.serversurvival.web.cuentasweb._shared.domain.CuentasWebRepository;
import es.serversurvival.web.cuentasweb._shared.infrastructure.MySQLCuentasWebRepository;
import es.serversurvival.deudas._shared.newformat.application.DeudasService;
import es.serversurvival.deudas._shared.newformat.domain.DeudasRepository;
import es.serversurvival.deudas._shared.newformat.infrastructure.MySQLDeudasRepository;
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
import es.serversurvival.web.webconnection.RabbitMQConsumerTask;
import es.serversurvival._shared.scoreboards.ScoreBoardManager;
import es.serversurvival._shared.scoreboards.ScoreboardUpdateTask;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

import static org.bukkit.ChatColor.*;

/**
 * Updated:
 *
 1 Jugadores
 2 Mensajes
 3 Transacciones
 4 Deudas
 5 Tienda
 6 Empresas
 7 Empleados
 8 CuentasWeb
 9 Verificacion cuentas
 10 OrdenesPremarket
 11 PosicionesAbiertas
 12 Llamadas api
 13 Ofertas empresas server
 14 Acciones empresas server
 */
public final class Pixelcoin extends JavaPlugin {
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
            put(JugadoresRepository.class, new MySQLJugadoresRepository(mysqlCOnfiguration));
            put(MensajesRepository.class, new MySQLJugadoresRepository(mysqlCOnfiguration));
            put(TransaccionesRepository.class, new MySQLJugadoresRepository(mysqlCOnfiguration));
            put(DeudasRepository.class, new MySQLDeudasRepository(mysqlCOnfiguration));
            put(TiendaRepository.class, new MySQLDeudasRepository(mysqlCOnfiguration));
            put(EmpresasRepostiory.class, new MySQLEmpresasRepository(mysqlCOnfiguration));
            put(EmpleadosRepository.class, new MySQLEmpresasRepository(mysqlCOnfiguration));
            put(CuentasWebRepository.class, new MySQLCuentasWebRepository(mysqlCOnfiguration));
            put(ConversacionesWebRepostiory.class, new InMemoryConversacionesRepository());
            put(VerificacionCuentaRepository.class, new InMemoryVerificacionCuentaRepository());
            put(OrderesPremarketRepository.class, new MySQLOrdenesPremarketRepository(mysqlCOnfiguration));
            put(PosicionesAbiertasRepository.class, new MySQLPosicionesAbiertasRepository(mysqlCOnfiguration));
            put(PosicionesCerradasRepository.class, new MySQLPosicionesCerradasRepository(mysqlCOnfiguration));
            put(ActivoInfoCacheRepository.class, new InMemoryActivoInfoCacheRepository());
            put(OfertasAccionesServerRepository.class, new MySQLOfertasAccionesServerRepository(mysqlCOnfiguration));
            put(AccionistasEmpresasServerRepository.class, new MySQLAccionistasEmpresasServerRepository(mysqlCOnfiguration));
        }});

        DependecyContainer.addAll(new HashMap<>(){{
            put(JugadoresService.class, new JugadoresService());
            put(MensajesService.class, new MensajesService());
            put(TransaccionesService.class, new TransaccionesService());
            put(DeudasService.class, new DeudasService());
            put(TiendaService.class, new TiendaService());
            put(EmpresasService.class, new EmpresasService());
            put(EmpleadosService.class, new EmpleadosService());
            put(CuentasWebService.class, new CuentasWebService());
            put(ConversacionesWebService.class, new ConversacionesWebService());
            put(VerificacionCuentaService.class, new VerificacionCuentaService());
            put(OrdenesPremarketService.class, new OrdenesPremarketService());
            put(PosicionesAbiertasSerivce.class, new PosicionesAbiertasSerivce());
            put(PosicionesCerradasService.class, new PosicionesCerradasService());
            put(ActivoInfoService.class, new ActivoInfoService());
            put(OfertasAccionesServerService.class, new OfertasAccionesServerService());
            put(AccionistasEmpresasServerService.class, new AccionistasEmpresasServerService());
        }});
    }
}
