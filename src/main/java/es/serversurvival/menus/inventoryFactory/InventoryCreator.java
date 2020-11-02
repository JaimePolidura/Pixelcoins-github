package es.serversurvival.menus.inventoryFactory;

import es.serversurvival.util.Funciones;
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
        ItemMeta comprarm = comprar.getItemMeta();
        comprarm.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "COMPRAR " + alias.toUpperCase());
        ArrayList<String> lista = new ArrayList<>();
        lista.add(ChatColor.GOLD + "Comprar 1 " + alias + " de " + simbolo + " a " + ChatColor.GREEN + formatea.format(precioUnidad) + " PC");
        comprarm.setLore(lista);
        comprar.setItemMeta(comprarm);

        ItemStack cancelar = new ItemStack(Material.RED_WOOL);
        ItemMeta cancelarm = cancelar.getItemMeta();
        cancelarm.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "CANCELAR");
        cancelar.setItemMeta(cancelarm);

        ItemStack mas1 = new ItemStack(Material.LIGHT_GRAY_BANNER);
        ItemMeta mas1m = mas1.getItemMeta();
        mas1m.setDisplayName(ChatColor.GREEN + "+1");
        mas1.setItemMeta(mas1m);

        ItemStack mas5 = new ItemStack(Material.LIGHT_GRAY_BANNER);
        ItemMeta mas5m = mas5.getItemMeta();
        mas5m.setDisplayName(ChatColor.GREEN + "+5");
        mas5.setItemMeta(mas5m);

        ItemStack mas10 = new ItemStack(Material.LIGHT_GRAY_BANNER);
        ItemMeta mas10m = mas10.getItemMeta();
        mas10m.setDisplayName(ChatColor.GREEN + "+10");
        mas10.setItemMeta(mas10m);


        ItemStack menos1 = new ItemStack(Material.LIGHT_GRAY_BANNER);
        ItemMeta menos1m = menos1.getItemMeta();
        menos1m.setDisplayName(ChatColor.RED + "-1");
        menos1.setItemMeta(menos1m);

        ItemStack menos5 = new ItemStack(Material.LIGHT_GRAY_BANNER);
        ItemMeta menos5m = menos5.getItemMeta();
        menos5m.setDisplayName(ChatColor.RED + "-5");
        menos5.setItemMeta(menos5m);

        ItemStack menos10 = new ItemStack(Material.LIGHT_GRAY_BANNER);
        ItemMeta menos10m = menos10.getItemMeta();
        menos10m.setDisplayName(ChatColor.RED + "-10");
        menos10.setItemMeta(menos10m);

        inventory.setItem(9, menos10);
        inventory.setItem(10, menos5);
        inventory.setItem(11, menos1);

        inventory.setItem(12, cancelar);
        inventory.setItem(14, comprar);

        inventory.setItem(15, mas1);
        inventory.setItem(16, mas5);
        inventory.setItem(17, mas10);

        return inventory;
    }

    public static Inventory createSolicitud (String titulo, String nombreItemAceptar, List<String> loreItemAceptar,
                                             String nombreItemCancelar, List<String> loreItemCancelar){
        Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, titulo);

        ItemStack aceptar = new ItemStack(Material.GREEN_WOOL);
        ItemMeta aceptarMeta = aceptar.getItemMeta();
        aceptarMeta.setDisplayName(nombreItemAceptar);
        aceptarMeta.setLore(loreItemAceptar);
        aceptar.setItemMeta(aceptarMeta);


        ItemStack cancelar = new ItemStack(Material.RED_WOOL);
        ItemMeta cancelarMeta = cancelar.getItemMeta();
        cancelarMeta.setDisplayName(nombreItemCancelar);
        cancelarMeta.setLore(loreItemCancelar);
        cancelar.setItemMeta(cancelarMeta);

        inventory.setItem(0, aceptar);
        inventory.setItem(4, cancelar);

        return inventory;
    }
}
