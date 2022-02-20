package es.serversurvival.deudas.prestar;

import es.serversurvival._shared.menus.Menu;
import es.serversurvival._shared.menus.MenuManager;
import es.serversurvival._shared.menus.solicitudes.Solicitud;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival._shared.menus.inventory.InventoryCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.List;

public class PrestamoSolicitud extends Menu implements Solicitud {
    private final PrestarUseCase prestarUseCase = PrestarUseCase.INSTANCE;

    private final Player player;
    private final Inventory inventory;
    private final String enviador;
    private final String destinatario;
    private final int pixelcoins;
    private final int dias;
    private final int interes;
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
        String descAceptarString = ChatColor.GOLD + "Prestamo de " + enviador + " de " + ChatColor.GREEN + AllMySQLTablesInstances.formatea.format(pixelcoins) + ChatColor.GOLD + " a " + dias + " dias  con un interes del " + interes + "% (" + ChatColor.GREEN + AllMySQLTablesInstances.formatea.format(Funciones.aumentarPorcentaje(pixelcoins, interes)) + " PC" + ChatColor.GOLD + ")";
        List<String> loreAceptar = Funciones.dividirDesc(descAceptarString, 40);
        String nombreCancelar = ChatColor.RED + "" + ChatColor.BOLD + "DENEGAR";

        this.inventory = InventoryCreator.createSolicitud(titulo, nombreItemAceptar, loreAceptar, nombreCancelar, Collections.emptyList());

        isClicked = false;

        enviarSolicitud();
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

        this.prestarUseCase.prestar(this.enviador, this.destinatario, pixelcoins, interes, dias);

        destinatarioPlayer.sendMessage(ChatColor.GOLD + "Has aceptado la solicitud de: " + ChatColor.GREEN + AllMySQLTablesInstances.formatea.format(pixelcoins) + " PC " + ChatColor.GOLD + "con un interes del: " + ChatColor.GREEN + interes +
                ChatColor.GOLD + " a " + ChatColor.GREEN + dias + ChatColor.GOLD + " dias");

        if (enviadorPlayer != null) {
            enviadorPlayer.sendMessage(ChatColor.GOLD + enviadorPlayer.getName() + " Te ha aceptado la solicitud de deuda");
            enviadorPlayer.playSound(enviadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        }

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
