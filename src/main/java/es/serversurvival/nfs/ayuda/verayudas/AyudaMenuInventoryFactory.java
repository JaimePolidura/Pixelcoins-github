package es.serversurvival.nfs.ayuda.verayudas;

import es.jaimetruman.ItemBuilder;
import es.serversurvival.nfs.shared.menus.inventory.InventoryFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
        return ItemBuilder.of(Material.WRITABLE_BOOK).title(ChatColor.GOLD + "" + ChatColor.BOLD + tiposAyuda).build();
    }
}
