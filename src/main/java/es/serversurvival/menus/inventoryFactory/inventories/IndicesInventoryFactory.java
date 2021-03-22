package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndicesInventoryFactory extends InventoryFactory {
    private Map<String, String> indices;

    public IndicesInventoryFactory () {
        indices = new HashMap<>();
        indices.put("GSPC", "S&P 500");
        indices.put("IBEX", "Ibex 35");
        indices.put("NDX", "Nasdaq 100");
        indices.put("RUT", "Russell 2000");
    }

    @Override
    protected Inventory buildInventory(String jugador) {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, ChatColor.DARK_RED + "" + ChatColor.BOLD + "INDICES");

        indices.forEach( (simbolo, nombre) -> {
            ItemStack item = new ItemStack(Material.WRITABLE_BOOK);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + nombre);

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + "Simbolo: " + simbolo);
            lore.add(ChatColor.RED + "Cargando...");

            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);

            inventory.addItem(item);
        });

        ItemStack info = new ItemStack(Material.PAPER);
        ItemMeta infoMeta = info.getItemMeta();

        infoMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "INFO");
        List<String> lore = new ArrayList<>();
        lore.add("Los indices componen una media de");
        lore.add("muchos valores de un pais. Para ver");
        lore.add("tu cartera de acciones: /bolsa cartera");

        infoMeta.setLore(lore);
        info.setItemMeta(infoMeta);

        inventory.addItem(info);

        return inventory;
    }

}