package es.serversurvival.menus.menus;

import com.mojang.datafixers.optics.profunctors.Closed;
import es.serversurvival.menus.Menu;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.menus.inventoryFactory.inventories.EmpresasVerTodasInventoryFactory;
import es.serversurvival.mySQL.Empresas;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.*;

public class EmpresasVerTodasMenu extends Menu implements CanGoBack, Clickable{
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
        EmpresasOwnerMenu menu = new EmpresasOwnerMenu(player);
        menu.openMenu();
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack itemClickeado = event.getCurrentItem();
        if(itemClickeado == null || Funciones.esDeTipoItem(itemClickeado, "AIR")){
            return;
        }
        List<String> lore = itemClickeado.getItemMeta().getLore();
        if(lore == null || lore.get(1) == null){
            return;
        }

        MySQL.conectar();

        String nombreEmpresa = lore.get(1).split(" ")[1].substring(4);
        empresasMySQL.solicitarServicio((Player) event.getWhoClicked(), nombreEmpresa);

        closeMenu();
        MySQL.desconectar();
    }
}
