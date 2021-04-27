package es.serversurvival.nfs.empresas.vender;

import es.serversurvival.nfs.shared.menus.Menu;
import es.serversurvival.nfs.shared.menus.solicitudes.Solicitud;
import es.serversurvival.nfs.utils.Funciones;
import es.serversurvival.nfs.shared.menus.inventory.InventoryCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.List;

public class VenderSolicitud extends Menu implements Solicitud {
    private final VenderEmpresaUseCase useCase = VenderEmpresaUseCase.INSTANCE;

    private final Inventory inventory;
    private final Player player;
    private final String enviador;
    private final String destinatario;
    private final String empresa;
    private final double precio;
    private boolean isClicked = false;

    public VenderSolicitud(String enviador, String destinatario, String empresa, double precio) {
        this.enviador = enviador;
        this.destinatario = destinatario;
        this.empresa = empresa;
        this.precio = precio;
        this.player = Bukkit.getPlayer(destinatario);

        String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "solicitud compra empresa";
        String nombreAceptar = ChatColor.GREEN + "" + ChatColor.BOLD + "ACEPTAR";
        String desc = ChatColor.GOLD + "Solicitud para comprar a " + enviador + " su empresa " + empresa + " a " + ChatColor.GREEN + "" + precio + " PC";
        List<String> loreAceptar = Funciones.dividirDesc(desc, 40);
        String cancelarNombre = ChatColor.RED + "" + ChatColor.BOLD + "DENEGAR";

        this.inventory = InventoryCreator.createSolicitud(titulo, nombreAceptar, loreAceptar, cancelarNombre, Collections.emptyList());
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
        openMenu();

        Player enviadorPlayer = Bukkit.getPlayer(this.enviador);
        enviadorPlayer.sendMessage(ChatColor.GOLD + "Has enviado la solicitud");
    }

    @Override
    public void aceptar() {
        isClicked = true;

        useCase.vender(enviador, destinatario, precio, empresa);

        player.sendMessage(ChatColor.GOLD + "Ahora eres dueño de " + ChatColor.DARK_AQUA + empresa + ChatColor.GOLD + " ,la has comprado por " + ChatColor.GREEN + precio + " PC");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Player vendedor = Bukkit.getPlayer(enviador);

        vendedor.sendMessage(ChatColor.GOLD + player.getName() + " te ha comprado " + ChatColor.DARK_AQUA + empresa + ChatColor.GOLD + " por " + ChatColor.GREEN + precio + " PC " + ChatColor.GOLD + ", ahora ya no eres dueño");
        vendedor.playSound(vendedor.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        closeMenu();
    }

    @Override
    public void cancelar() {
        isClicked = true;

        Player enviador = Bukkit.getPlayer(this.enviador);

        if (enviador != null) {
            enviador.sendMessage(ChatColor.GOLD + "" + this.destinatario + "te ha cancelado la solicitud");
        }

        player.sendMessage(ChatColor.GOLD + "Has cancelado la solicitud");

        closeMenu();
    }

    @Override
    public void onCloseInventory(InventoryCloseEvent event) {
        if(!isClicked && Bukkit.getPlayer(enviador) != null){
            Bukkit.getPlayer(enviador).sendMessage(ChatColor.GOLD + destinatario + " ha cancelado la solicitud");
        }
    }
}
