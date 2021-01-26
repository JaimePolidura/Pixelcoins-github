package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.menus.menus.ElegirInversionMenu;
import es.serversurvival.util.MinecraftUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class ElegirInversionInventoryFactory extends InventoryFactory {
    @Override
    protected Inventory buildInventory(String player) {
        return buildInventory();
    }

    private Inventory buildInventory () {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, ElegirInversionMenu.titulo);

        inventory.setItem(0, MinecraftUtils.displayname(Material.PAPER, ChatColor.GOLD + "" + ChatColor.BOLD + "Acciones"));
        inventory.setItem(2, MinecraftUtils.displayname(Material.GOLD_INGOT, ChatColor.GOLD + "" + ChatColor.BOLD + "Criptomonedas"));
        inventory.setItem(4, MinecraftUtils.displayname(Material.CHARCOAL, ChatColor.GOLD + "" + ChatColor.BOLD + "Materias primas"));

        return inventory;
    }
}
