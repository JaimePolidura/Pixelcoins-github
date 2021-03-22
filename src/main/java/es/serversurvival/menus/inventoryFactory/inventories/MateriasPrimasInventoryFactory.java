package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.menus.menus.MateriasPrimasMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MateriasPrimasInventoryFactory extends InventoryFactory {
    private HashMap<String, String> materiasPrimas = new HashMap<>();

    @Override
    protected Inventory buildInventory(String player) {
        buildPag1();

        return buildInv();
    }

    private Inventory buildInv() {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, MateriasPrimasMenu.titulo);

        ItemStack anadir = new ItemStack(Material.COAL);
        ItemMeta itemMeta = anadir.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        for (Map.Entry<String, String> entry : materiasPrimas.entrySet()) {
            itemMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + entry.getValue());
            lore.add(ChatColor.GOLD + "Simbolo: " + entry.getKey());
            lore.add(ChatColor.RED + "Cargando...");
            itemMeta.setLore(lore);
            anadir.setItemMeta(itemMeta);
            inventory.addItem(anadir);
            lore.clear();
        }

        ItemStack papel = new ItemStack(Material.PAPER);
        ItemMeta infoMeta = papel.getItemMeta();
        ArrayList<String> info = new ArrayList<>();
        info.add("Para invertir en estas materias primas clickea en cualquiera de ellas y elige la cantidad a comprar");
        info.add("                  ");
        info.add("AVISO: ¡Estas materias primas van con unos 15 minutos aproximadamente de retraso! ");
        infoMeta.setLore(info);
        infoMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "INFO");
        papel.setItemMeta(infoMeta);
        inventory.setItem(4, papel);

        return inventory;
    }

    private void buildPag1(){
        materiasPrimas.put("DCOILBRENTEU", "Petroleo (Brent)");
        materiasPrimas.put("DHHNGSP", "Gas natural");
        materiasPrimas.put("DJFUELUSGULF", "Queroseno (Combustible aviones)");
        materiasPrimas.put("GASDESW","Diesel");
    }
}