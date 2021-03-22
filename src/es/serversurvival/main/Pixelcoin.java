package es.serversurvival.main;

import es.serversurvival.objetos.*;
import es.serversurvival.eventos.PlayerCommand;
import es.serversurvival.eventos.PlayerInteract;
import es.serversurvival.eventos.PlayerInventoryClick;
import es.serversurvival.eventos.PlayerJoin;
import es.serversurvival.task.MensajeWeb;
import es.serversurvival.task.ScoreboardPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Score;

public class Pixelcoin extends JavaPlugin implements Listener {
    private Solicitudes s = new Solicitudes();
    private Deudas d = new Deudas();
    private Empleados em = new Empleados();
    private MySQL sql = new MySQL();
    private ScoreboardPlayer sp = new ScoreboardPlayer();

    @Override
    public void onEnable() {
        getLogger().info("------------Plugin activado -------------");
        this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "------------------------------");
        this.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "       Pixelcoins POO");
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new PlayerInventoryClick(), this);
        this.setUpCommands(new PlayerCommand());

        try {
            sql.conectar();
        } catch (Exception e) {
            this.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "Error al conectar a la base de datos");
        }

        try {
            s.conectar();
            s.setUp(this.getServer());
            s.desconectar();
        } catch (Exception e) {
            this.getServer().getConsoleSender().sendMessage("[PixelCoins] " + ChatColor.DARK_RED + "Error en borrar las solicitudes (La base de datos puede estar apagada)");
        }

        try {
            d.conectar();
            d.pagarDeudas(this.getServer());
            d.desconectar();
        } catch (Exception e) {
            this.getServer().getConsoleSender().sendMessage("[PixelCoins] " + ChatColor.DARK_RED + "Error en pagar deudas (La base de datos puede estar apagada)");
        }

        try {
            em.conectar();
            em.pagarSueldos(this.getServer());
            em.desconectar();
        } catch (Exception e) {
            this.getServer().getConsoleSender().sendMessage("[PixelCoins] " + ChatColor.DARK_RED + "Error en pagar sueldos (La base de datos puede estar apagada)");
        }

        MensajeWeb mensajeWeb = new MensajeWeb(this.getServer());
        mensajeWeb.runTaskTimer(this, 0, 20 * MensajeWeb.delay);

        sp.runTaskTimer(this, 0, 20 * ScoreboardPlayer.scoreboardSwitchDelay);
        this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "------------------------------");
    }

    @Override
    public void onDisable() {
        sql.desconectar();
        getLogger().info("----------- Plugin desactivado -----------");
    }

    private void setUpCommands(PlayerCommand commandEx) {
        getCommand("dinero").setExecutor(commandEx);
        getCommand("pagar").setExecutor(commandEx);
        getCommand("topricos").setExecutor(commandEx);
        getCommand("toppobres").setExecutor(commandEx);
        getCommand("tienda").setExecutor(commandEx);
        getCommand("vender").setExecutor(commandEx);
        getCommand("ayuda").setExecutor(commandEx);
        getCommand("estadisticas").setExecutor(commandEx);
        getCommand("topvendedores").setExecutor(commandEx);
        getCommand("prestar").setExecutor(commandEx);
        getCommand("aceptar").setExecutor(commandEx);
        getCommand("rechazar").setExecutor(commandEx);
        getCommand("deudas").setExecutor(commandEx);
        getCommand("topfiables").setExecutor(commandEx);
        getCommand("topmenosfiables").setExecutor(commandEx);
        getCommand("crearempresa").setExecutor(commandEx);
        getCommand("depositar").setExecutor(commandEx);
        getCommand("sacar").setExecutor(commandEx);
        getCommand("logotipo").setExecutor(commandEx);
        getCommand("empresas").setExecutor(commandEx);
        getCommand("contratar").setExecutor(commandEx);
        getCommand("despedir").setExecutor(commandEx);
        getCommand("aceptarTrabajo").setExecutor(commandEx);
        getCommand("rechazarTrabajo").setExecutor(commandEx);
        getCommand("irse").setExecutor(commandEx);
        getCommand("editarEmpleado").setExecutor(commandEx);
        getCommand("venderempresa").setExecutor(commandEx);
        getCommand("aceptarcompra").setExecutor(commandEx);
        getCommand("rechazarcompra").setExecutor(commandEx);
        getCommand("miempresa").setExecutor(commandEx);
        getCommand("mensajes").setExecutor(commandEx);
        getCommand("editarnombre").setExecutor(commandEx);
        getCommand("mistrabajos").setExecutor(commandEx);
        getCommand("borrarempresa").setExecutor(commandEx);
        getCommand("confirmarborrar").setExecutor(commandEx);
        getCommand("cancelarborrar").setExecutor(commandEx);
        getCommand("comprar").setExecutor(commandEx);
        getCommand("editardescripccion").setExecutor(commandEx);
        getCommand("cancelardeuda").setExecutor(commandEx);
        getCommand("pagardeuda").setExecutor(commandEx);
    }

}