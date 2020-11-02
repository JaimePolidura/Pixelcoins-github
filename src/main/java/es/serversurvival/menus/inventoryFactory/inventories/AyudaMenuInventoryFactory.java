package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AyudaMenuInventoryFactory extends InventoryFactory {
    @Override
    protected Inventory buildInventory(String jugador) {
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.DARK_RED + "" + ChatColor.BOLD + "CLICK para ver las ayudas");

        inventory.setItem(5, buildItemAyuda("BOLSA"));
        inventory.setItem(9, buildItemAyuda("JUGAR"));
        inventory.setItem(11, buildItemAyuda("PIXELCOINS"));
        inventory.setItem(13, buildItemAyuda("NORMAS"));
        inventory.setItem(15, buildItemAyuda("TIENDA"));
        inventory.setItem(17, buildItemAyuda("DEUDA"));
        inventory.setItem(21, buildItemAyuda("EMPRESARIO"));
        inventory.setItem(23, buildItemAyuda("EMPLEO"));

        return inventory;
    }

    private ItemStack buildItemAyuda (String tiposAyuda) {
        ItemStack ayuda = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta auyudaMeta = ayuda.getItemMeta();

        auyudaMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + tiposAyuda);
        ayuda.setItemMeta(auyudaMeta);

        return ayuda;
    }
}
