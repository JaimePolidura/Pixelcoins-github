package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.menus.menus.ElegirInversionMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ElegirInversionInventoryFactory extends InventoryFactory {
    @Override
    protected Inventory buildInventory(String player) {
        return buildInventory();
    }

    private Inventory buildInventory () {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, ElegirInversionMenu.titulo);

        ItemStack acciones = new ItemStack(Material.PAPER);
        ItemMeta itemMetaAcciones = acciones.getItemMeta();
        itemMetaAcciones.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Acciones");
        acciones.setItemMeta(itemMetaAcciones);
        inventory.setItem(0, acciones);

        ItemStack criptomonedas = new ItemStack(Material.GOLD_INGOT);
        ItemMeta itemMetaCripto = criptomonedas.getItemMeta();
        itemMetaCripto.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Criptomonedas");
        criptomonedas.setItemMeta(itemMetaCripto);
        inventory.setItem(2, criptomonedas);

        ItemStack materiasPrimas = new ItemStack(Material.CHARCOAL);
        ItemMeta itemMeta = materiasPrimas.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Materias primas");
        materiasPrimas.setItemMeta(itemMeta);
        inventory.setItem(4, materiasPrimas);

        return inventory;
    }
}
