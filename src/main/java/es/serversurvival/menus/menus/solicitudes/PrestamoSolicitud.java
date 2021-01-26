package es.serversurvival.menus.menus.solicitudes;

import es.serversurvival.mySQL.enums.TipoTransaccion;
import es.serversurvival.util.Funciones;
import es.serversurvival.menus.Menu;
import es.serversurvival.menus.MenuManager;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.task.ScoreBoardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.List;

public class PrestamoSolicitud extends Menu implements Solicitud {
    private Player player;
    private Inventory inventory;
    private String enviador;
    private String destinatario;
    private int pixelcoins;
    private int dias;
    private int interes;
    public boolean isClicked;

    public PrestamoSolicitud(String enviador, String destinatario, int pixelcoins, int dias, int interes) {
        this.enviador = enviador;
        this.destinatario = destinatario;
        this.pixelcoins = pixelcoins;
        this.dias = dias;
        this.interes = interes;
        this.player = Bukkit.getPlayer(destinatario);

        String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "    Solicitud Prestamo";
        String nombreItemAceptar = ChatColor.GREEN + "" + ChatColor.BOLD + "ACEPTAR";
        String descAceptarString = ChatColor.GOLD + "Prestamo de " + enviador + " de " + ChatColor.GREEN + formatea.format(pixelcoins) + ChatColor.GOLD + " a " + dias + " dias  con un interes del " + interes + "% (" + ChatColor.GREEN + formatea.format(Funciones.aumentarPorcentaje(pixelcoins, interes)) + " PC" + ChatColor.GOLD + ")";
        List<String> loreAceptar = Funciones.dividirDesc(descAceptarString, 40);
        String nombreCancelar = ChatColor.RED + "" + ChatColor.BOLD + "DENEGAR";

        this.inventory = InventoryCreator.createSolicitud(titulo, nombreItemAceptar, loreAceptar, nombreCancelar, Collections.emptyList());

        isClicked = false;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getDestinatario() {
        return destinatario;
    }

    @Override
    public void enviarSolicitud() {
        Player destinatarioPlayer = Bukkit.getPlayer(this.destinatario);
        Player enviadorPlayer = Bukkit.getPlayer(this.enviador);

        openMenu();
        enviadorPlayer.sendMessage(ChatColor.GOLD + "Has enviado la solicitud");
    }

    @Override
    public void aceptar() {
        isClicked = true;

        Player enviadorPlayer = Bukkit.getPlayer(this.enviador);
        Player destinatarioPlayer = Bukkit.getPlayer(this.destinatario);
        transaccionesMySQL.conectar();

        transaccionesMySQL.realizarTransferencia(enviador, destinatario, pixelcoins, "", TipoTransaccion.DEUDAS_PRIMERPAGO);
        deudasMySQL.nuevaDeuda(destinatario, enviador, Funciones.aumentarPorcentaje(pixelcoins, interes), dias, interes);

        transaccionesMySQL.desconectar();


        destinatarioPlayer.sendMessage(ChatColor.GOLD + "Has aceptado la solicitud de: " + ChatColor.GREEN + formatea.format(pixelcoins) + " PC " + ChatColor.GOLD + "con un interes del: " + ChatColor.GREEN + interes +
                ChatColor.GOLD + " a " + ChatColor.GREEN + dias + ChatColor.GOLD + " dias");

        if (enviadorPlayer != null) {
            ScoreBoardManager.getInstance().updateScoreboard(enviadorPlayer);
            enviadorPlayer.sendMessage(ChatColor.GOLD + enviadorPlayer.getName() + " Te ha aceptado la solicitud de deuda");
            enviadorPlayer.playSound(enviadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        }
        ScoreBoardManager.getInstance().updateScoreboard(destinatarioPlayer);


        closeMenu();
    }

    @Override
    public void cancelar() {
        Player enviadorPlayer = Bukkit.getPlayer(enviador);
        Player destinatarioPlayer = Bukkit.getPlayer(destinatario);

        destinatarioPlayer.sendMessage(ChatColor.GOLD + "Has cancelado la solicitud");

        if (enviadorPlayer != null) {
            enviadorPlayer.sendMessage(ChatColor.GOLD + destinatarioPlayer.getName() + " te ha cancelado la solicitud!");
            enviadorPlayer.playSound(destinatarioPlayer.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        }

        isClicked = true;
        closeMenu();
    }

    @Override
    public void onCloseInventory(InventoryCloseEvent event) {
        if(!isClicked && Bukkit.getPlayer(enviador) != null){
            Bukkit.getPlayer(enviador).sendMessage(ChatColor.GOLD + destinatario + " te ha cancelado la solicitud");
            MenuManager.borrarMenu(destinatario);
        }
    }
}
