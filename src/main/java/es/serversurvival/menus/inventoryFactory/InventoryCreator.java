package es.serversurvival.menus.inventoryFactory;

import es.serversurvival.util.Funciones;
import es.serversurvival.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class InventoryCreator {

    public static Inventory createInventoryMenu(InventoryFactory factory, String player) {
        return factory.buildInventory(player);
    }

    public static Inventory createConfirmacionAumento (String alias, String simbolo, double precioUnidad) {
        DecimalFormat formatea = Funciones.FORMATEA;
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.DARK_RED + "" + ChatColor.BOLD + "   SELECCIONA " + alias.toUpperCase());

        ItemStack comprar = new ItemStack(Material.GREEN_WOOL);
        String displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "COMPRAR " + alias.toUpperCase();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + "Comprar 1 " + alias + " de " + simbolo + " a " + ChatColor.GREEN + formatea.format(precioUnidad) + " PC");
        inventory.setItem(14, ItemBuilder.loreDisplayName(Material.GREEN_WOOL, displayName, lore));

        inventory.setItem(12, ItemBuilder.displayname(Material.RED_WOOL, ChatColor.RED + "" + ChatColor.BOLD + "CANCELAR"));
        inventory.setItem(15, ItemBuilder.displayname(Material.LIGHT_GRAY_BANNER, ChatColor.GREEN + "+1"));
        inventory.setItem(16, ItemBuilder.displayname(Material.LIGHT_GRAY_BANNER, ChatColor.GREEN + "+5"));
        inventory.setItem(17, ItemBuilder.displayname(Material.LIGHT_GRAY_BANNER, ChatColor.GREEN + "+10"));
        inventory.setItem(11, ItemBuilder.displayname(Material.LIGHT_GRAY_BANNER, ChatColor.RED + "-1"));
        inventory.setItem(10, ItemBuilder.displayname(Material.LIGHT_GRAY_BANNER, ChatColor.RED + "-5"));
        inventory.setItem(9, ItemBuilder.displayname(Material.LIGHT_GRAY_BANNER, ChatColor.RED + "-10"));

        return inventory;
    }

    public static Inventory createSolicitud (String titulo, String nombreItemAceptar, List<String> loreItemAceptar,
                                             String nombreItemCancelar, List<String> loreItemCancelar){
        Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, titulo);

        inventory.setItem(0, ItemBuilder.loreDisplayName(Material.GREEN_WOOL, nombreItemAceptar, loreItemAceptar));
        inventory.setItem(4, ItemBuilder.loreDisplayName(Material.RED_WOOL, nombreItemCancelar, loreItemCancelar));

        return inventory;
    }
}
