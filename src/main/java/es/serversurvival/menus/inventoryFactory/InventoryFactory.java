package es.serversurvival.menus.inventoryFactory;

import es.serversurvival.menus.menus.Paginated;
import es.serversurvival.mySQL.*;
import es.serversurvival.util.Funciones;
import es.serversurvival.util.MinecraftUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;

public abstract class InventoryFactory implements AllMySQLTablesInstances {
    protected abstract Inventory buildInventory (String jugador);

    protected ItemStack buildItemGoBack () {
        ItemStack back = new ItemStack(Material.RED_WOOL);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "<--");

        back.setItemMeta(backMeta);
        return back;
    }

    protected ItemStack buildItemFordward () {
        return MinecraftUtils.displayname(Material.GREEN_WOOL, Paginated.ITEM_NAME_GOFORDWARD);
    }
}
