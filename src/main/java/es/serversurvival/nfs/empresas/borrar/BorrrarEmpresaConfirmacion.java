package es.serversurvival.nfs.empresas.borrar;

import es.serversurvival.legacy.menus.Menu;
import es.serversurvival.legacy.menus.menus.confirmaciones.Confirmacion;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.util.Funciones;
import es.serversurvival.legacy.menus.inventoryFactory.InventoryCreator;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.*;

public class BorrrarEmpresaConfirmacion extends Menu implements Confirmacion {
    private final EmpresaBorrarUseCase useCase = EmpresaBorrarUseCase.INSTANCE;

    private final Player player;
    private final Inventory inventory;
    private final String empresa;

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
        useCase.borrar(player.getName(), empresa);

        player.sendMessage(ChatColor.GOLD + "Has borrado tu empresa, has recibido todas las pixelcoins de ello");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        closeMenu();
    }

    @Override
    public void cancelar() {
        player.sendMessage(ChatColor.GOLD + "Has cancelado");

        closeMenu();
    }

    @Override public void onOtherClick(InventoryClickEvent event) {}
}
