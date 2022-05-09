package es.serversurvival.empresas.empleados.contratar;

import es.serversurvival.empresas.empleados._shared.domain.TipoSueldo;
import es.serversurvival._shared.menus.Menu;
import es.serversurvival._shared.menus.MenuManager;
import es.serversurvival._shared.menus.inventory.InventoryCreator;
import es.serversurvival._shared.menus.solicitudes.Solicitud;
import es.serversurvival._shared.utils.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class ContratarSolicitud extends Menu implements Solicitud {
    private final ContratarUseCase useCase;

    private final Player player;
    private final Inventory inventory;
    private final String enviador;
    private final String destinatario;
    private final String empresa;
    private final double sueldo;
    private final TipoSueldo tipoSueldo;
    private final String cargo;
    private boolean isClicked = false;

    public ContratarSolicitud (String enviador, String destinatario, String empresa, double sueldo, TipoSueldo tipoSueldo, String cargo) {
        this.useCase = new ContratarUseCase();
        this.player = Bukkit.getPlayer(destinatario);
        this.enviador = enviador;
        this.destinatario = destinatario;
        this.empresa = empresa;
        this.sueldo = sueldo;
        this.tipoSueldo = tipoSueldo;
        this.cargo = cargo;

        String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "    Solicitud Contrato";
        String nombreAceptar = ChatColor.GREEN + "" + ChatColor.BOLD + "ACEPTAR";
        String descStrinAceptar = ChatColor.GOLD + "Solicitud de contrato de la empresa " + this.empresa + " para trabajar como " + this.cargo + " , con un sueldo de " + ChatColor.GREEN + this.sueldo + " PC" + ChatColor.GOLD + "/" + tipoSueldo.nombre;
        List<String> lore = Funciones.dividirDesc(descStrinAceptar, 40);

        String nombreCancelar = ChatColor.RED + "" + ChatColor.BOLD + "DENEGAR";

        this.inventory = InventoryCreator.createSolicitud(titulo, nombreAceptar, lore, nombreCancelar, Collections.emptyList());

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
        openMenu();

        Player enviador = Bukkit.getPlayer(this.enviador);
        enviador.sendMessage(ChatColor.GOLD + "Has enviado la solicitud");
    }

    @Override
    public void aceptar() {
        Player enviadorPlayer = Bukkit.getPlayer(enviador);
        Player destinatarioPlayer = Bukkit.getPlayer(destinatario);

        useCase.contratar(enviadorPlayer.getName(), destinatario, empresa, sueldo, tipoSueldo, cargo);

        closeMenu();

        if (enviadorPlayer != null) {
            enviadorPlayer.sendMessage(ChatColor.GOLD + "Has contratado a " + destinatario + ChatColor.AQUA + " /empresas despedir /empresas editarempleado");
        }
        destinatarioPlayer.sendMessage(ChatColor.GOLD + "Ahora trabajas para " + enviador + ChatColor.AQUA + " /empleos irse /empleos misempleos");

        isClicked = true;
    }

    @Override
    public void cancelar() {
        Player enviadorPlayer = Bukkit.getPlayer(enviador);
        Player destinatarioPlayer = Bukkit.getPlayer(destinatario);

        closeMenu();

        destinatarioPlayer.sendMessage(ChatColor.GOLD + "Has cancelado la solicitud");
        if (enviadorPlayer != null) {
            enviadorPlayer.sendMessage(ChatColor.GOLD + destinatarioPlayer.getName() + " te ha cancelado la solicitud");
        }

        isClicked = true;
    }

    @Override
    public void onCloseInventory(InventoryCloseEvent event) {
        if(!isClicked && Bukkit.getPlayer(enviador) != null){
            Bukkit.getPlayer(enviador).sendMessage(ChatColor.GOLD + destinatario + " te ha cancelado la solicitud");
            MenuManager.borrarMenu(destinatario);
        }
    }
}
