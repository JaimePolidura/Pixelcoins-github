package es.serversurvival.menus.menus.confirmaciones;

import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import es.serversurvival.menus.Menu;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.mySQL.Empresas;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.*;

public class BorrrarEmpresaConfirmacion extends Menu implements Confirmacion {
    private Player player;
    private Inventory inventory;
    private String empresa;

    public BorrrarEmpresaConfirmacion(Player destinatario, String empresa) {
        this.empresa = empresa;
        this.player = destinatario;

        String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "     Confirmar borrar";
        String nombreAceptar =  ChatColor.GREEN + "" + ChatColor.BOLD + "CONFIRMAR";
        String descAceptar = ChatColor.GOLD + "Confirmar para borrar la emprea: " + this.empresa + ", se te devolveran las pixel coins de la empresa a tu cuenta";
        String nombreCancelar = ChatColor.RED + "" + ChatColor.BOLD + "CANCELAR";

        List<String> loreAceptar = Funciones.dividirDesc(descAceptar, 40);

        this.inventory = InventoryCreator.createSolicitud(titulo, nombreAceptar, loreAceptar, nombreCancelar, Collections.emptyList());
        openMenu();
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
    public void confirmar() {
        MySQL.conectar();
        empresasMySQL.borrarEmpresaManual(empresa);
        MySQL.desconectar();

        player.sendMessage(ChatColor.GOLD + "Has borrado tu empresa, has recibido todas las pixelcoins de ello");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        closeMenu();
    }

    @Override
    public void cancelar() {
        player.sendMessage(ChatColor.GOLD + "Has cancelado");

        closeMenu();
    }

    @Override
    public void onOtherClick(InventoryClickEvent event) {return;}
}
