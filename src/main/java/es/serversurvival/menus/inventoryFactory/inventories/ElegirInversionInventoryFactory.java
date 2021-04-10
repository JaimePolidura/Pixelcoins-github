package es.serversurvival.menus.inventoryFactory.inventories;

import es.jaimetruman.ItemBuilder;
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

        ItemBuilder.of(Material.PAPER).title(ChatColor.GOLD + "" + ChatColor.BOLD + "Acciones").buildAddInventory(inventory, 0);
        ItemBuilder.of(Material.GOLD_INGOT).title(ChatColor.GOLD + "" + ChatColor.BOLD + "Criptomonedas").buildAddInventory(inventory, 2);
        ItemBuilder.of(Material.CHARCOAL).title(ChatColor.GOLD + "" + ChatColor.BOLD + "Materias primas").buildAddInventory(inventory, 4);

        return inventory;
    }
}
