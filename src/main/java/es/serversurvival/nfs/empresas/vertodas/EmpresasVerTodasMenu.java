package es.serversurvival.nfs.empresas.vertodas;

import es.serversurvival.legacy.menus.Menu;
import es.serversurvival.legacy.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.legacy.menus.menus.CanGoBack;
import es.serversurvival.legacy.menus.menus.Clickable;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.nfs.utils.Funciones;
import es.serversurvival.nfs.empresas.miempresa.EmpresasVerMenu;
import es.serversurvival.nfs.jugadores.perfil.PerfilMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.*;

public class EmpresasVerTodasMenu extends Menu implements CanGoBack, Clickable {
    public static final String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "        EMPRESAS";
    private Inventory inventory;
    private Player player;

    public EmpresasVerTodasMenu(Player player) {
        this.inventory = InventoryCreator.createInventoryMenu(new EmpresasVerTodasInventoryFactory(), player.getName());
        this.player = player;
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
    public String getNameItemGoBack() {
        return Material.RED_WOOL.toString();
    }

    @Override
    public void goBack() {
        PerfilMenu perfilMenu = new PerfilMenu(player);
    }

    @Override
    public void onOherClick(InventoryClickEvent event) {
        ItemStack itemClickeado = event.getCurrentItem();
        if(itemClickeado == null || Funciones.esDeTipoItem(itemClickeado, "AIR")){
            return;
        }
        List<String> lore = itemClickeado.getItemMeta().getLore();
        if(lore == null || lore.get(1) == null){
            return;
        }

        String nombreEmpresa = lore.get(1).split(" ")[1].substring(4);
        String displayName = itemClickeado.getItemMeta().getDisplayName();

        if(displayName.equalsIgnoreCase(ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA VER TU EMPRESA")){
            EmpresasVerMenu menu = new EmpresasVerMenu(player, nombreEmpresa);
        }else{
            AllMySQLTablesInstances.empresasMySQL.solicitarServicio((Player) event.getWhoClicked(), nombreEmpresa);
            closeMenu();
        }

    }
}
