package es.serversurvival.main;

import es.jaimetruman.commands.CommandMapper;
import es.serversurvival.eventosMinecraft.*;
import es.serversurvival.mySQL.*;
import es.serversurvival.socketWeb.ServerSocketWeb;
import es.serversurvival.task.*;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.ChatColor.*;

public final class Pixelcoin extends JavaPlugin {
    private ConversacionesWeb conversacionesWebMySQL = ConversacionesWeb.INSTANCE;
    private Deudas deudasMySQL = Deudas.INSTANCE;
    private Empleados empleadosMySQL = Empleados.INSTANCE;

    private static Pixelcoin plugin;

    public static Pixelcoin getInstance() {
        return plugin;
    }

    public void onEnable() {
        plugin = this;

        getLogger().info("------------Plugin activado -------------");
        getServer().getConsoleSender().sendMessage(GREEN + "------------------------------");

        MySQL.conectar();
        deudasMySQL.pagarDeudas();
        empleadosMySQL.pagarSueldos();
        conversacionesWebMySQL.borrarTodasConversacionesWeb();
        MySQL.desconectar();

        this.setUpCommands();

        getServer().getConsoleSender().sendMessage(GREEN + "------------------------------");
        setUpListeners();
        setUpTasks();
    }

    private void setUpTasks() {
        PagarDividendos dividendosTask = new PagarDividendos();
        PagarSueldos sueldosTask = new PagarSueldos();
        PagarDeudas deudasTask = new PagarDeudas();
        MensajesServer mensjesTask = new MensajesServer();
        ActualizarLlamadasApi actualizarPreciosTask = new ActualizarLlamadasApi();
        ActualizarNPCs actualizarNPCs = new ActualizarNPCs();
        SplitAcciones splitAccionesTask = new SplitAcciones();
        RabbitMQConsumerTask rabbitMQConsumer = new RabbitMQConsumerTask();
        OrdenesBolsa ordenesBolsaTask = new OrdenesBolsa();

        int cadaDia = 20 * 60 * 60 * 24;
        int cada20Minutos = 20 * 20 * 60;
        int cada5Minutos = 20 * 60 * 5;
        int cada3Minutos = 20 * 60 * 3;
        int cada2Minutos = 20 * 60 * 2;
        int cadaMinuto = 20 * 60;
        int cadaMinutoYMedio = 20 * 60 + 20 * 30;
        int cada15Segundos = 20 * 15;
        int ahora = 0;

        ScoreBoardManager.getInstance().runTaskTimer(this, ahora, cadaMinuto);
        sueldosTask.runTaskTimer(this, ahora, cadaDia);
        deudasTask.runTaskTimer(this, ahora, cadaDia);
        mensjesTask.runTaskTimer(this, ahora, cada20Minutos);
        actualizarPreciosTask.runTaskTimer(this, ahora, cada3Minutos);
        actualizarNPCs.runTaskTimer(this, cada15Segundos, cada5Minutos);
        splitAccionesTask.runTaskTimer(this, cadaMinuto, cadaDia);
        dividendosTask.runTaskTimer(this, cada2Minutos, cadaDia);
        ordenesBolsaTask.runTaskTimer(this, cadaMinutoYMedio, cadaMinuto);
        rabbitMQConsumer.runTaskAsynchronously(this);
    }

    private void setUpCommands () {
        CommandMapper.create(
                "es.serversurvival.comandos",
                DARK_RED + "Comando no encontrado /ayuda",
                DARK_RED + "Paara ejecutar ese comando, tienes que estar en el servidor"
        );
    }

    private void setUpListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new PlayerInventoryClick(), this);
        getServer().getPluginManager().registerEvents(new PlayerCloseInventory(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
    }
}
