package es.serversurvival.main;

import es.serversurvival.comandos.CommandManager;
import es.serversurvival.eventos.*;
import es.serversurvival.objetos.mySQL.*;
import es.serversurvival.objetos.task.LlamadasApiTask;
import es.serversurvival.objetos.task.MensajeWeb;
import es.serversurvival.objetos.task.ScoreboardTaskManager;
import org.bukkit.ChatColor;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class Pixelcoin extends JavaPlugin {
    private static Pixelcoin plugin;
    private PosicionesAbiertas posicionesAbiertas = new PosicionesAbiertas();
    private Deudas d = new Deudas();
    private Empleados em = new Empleados();
    private MySQL sql = new MySQL();

    public static Pixelcoin getInstance() {
        return plugin;
    }

    public void onEnable() {
        getLogger().info("------------Plugin activado -------------");
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "------------------------------");
        getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "       Pixelcoins POO");

        //Preparamos los comandos y dejamos al server a la escucha de los eventos
        setUpComandos(new CommandManager());
        setUpListeners();

        //Pago de sueldos de las empresas
        em.conectar();
        em.pagarSueldos();
        em.desconectar();

        //Pago de las deudas
        d.conectar();
        d.pagarDeudas();
        d.desconectar();

        //Pago de dividendos de acciones
        posicionesAbiertas.conectar();
        posicionesAbiertas.pagarDividendos();
        posicionesAbiertas.desconectar();

        //Iniciamos todas las tareas del plugin en el server
        MensajeWeb mensajeWeb = new MensajeWeb(this.getServer());
        mensajeWeb.runTaskTimer(this, 0, 20 * MensajeWeb.delay);
        ScoreboardTaskManager sp = new ScoreboardTaskManager();
        sp.runTaskTimer(this, 0, 20 * ScoreboardTaskManager.scoreboardSwitchDelay);
        LlamadasApiTask llamadasApiTask = new LlamadasApiTask();
        llamadasApiTask.runTaskTimer(this, 0, LlamadasApiTask.delay * 20);

        plugin = this;

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "------------------------------");
    }

    @Override
    public void onDisable() {
        sql.desconectar();
        getLogger().info("----------- Plugin desactivado -----------");
    }

    private void setUpListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new PlayerInventoryClick(), this);
        getServer().getPluginManager().registerEvents(new PlayerCloseInventory(), this);
    }

    private void setUpComandos(CommandExecutor commandExecutor) {
        this.getCommand("pagar").setExecutor(commandExecutor);
        this.getCommand("dinero").setExecutor(commandExecutor);
        this.getCommand("estadisticas").setExecutor(commandExecutor);
        this.getCommand("top").setExecutor(commandExecutor);
        this.getCommand("deudas").setExecutor(commandExecutor);
        this.getCommand("tienda").setExecutor(commandExecutor);
        this.getCommand("vender").setExecutor(commandExecutor);
        this.getCommand("mensajes").setExecutor(commandExecutor);
        this.getCommand("empresas").setExecutor(commandExecutor);
        this.getCommand("comprar").setExecutor(commandExecutor);
        this.getCommand("empleos").setExecutor(commandExecutor);
        this.getCommand("ayuda").setExecutor(commandExecutor);
        this.getCommand("bolsa").setExecutor(commandExecutor);
        this.getCommand("cuenta").setExecutor(commandExecutor);}
}