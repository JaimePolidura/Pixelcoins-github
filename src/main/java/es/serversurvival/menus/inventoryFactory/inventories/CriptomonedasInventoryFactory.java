package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.menus.menus.CriptomonedasMenu;
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

public class CriptomonedasInventoryFactory extends InventoryFactory {
    private Map<String, String> criptomonedas = new HashMap<>();

    @Override
    protected Inventory buildInventory(String player) {
        buildPag1();

        return buidlInv();
    }

    private Inventory buidlInv(){
        Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER , CriptomonedasMenu.titulo);

        ItemStack anadir = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta itemMeta = anadir.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        for (Map.Entry<String, String> entry : criptomonedas.entrySet()) {
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
        info.add("Para invertir en estas criptomonedas clickea en cualquiera de ellas y elige la cantidad a comprar");
        info.add("                  ");
        info.add("AVISO: ¡Estas criptomonedas van con unos 15 minutos aproximadamente de retraso! ");
        infoMeta.setLore(info);
        infoMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "INFO");
        papel.setItemMeta(infoMeta);
        inventory.setItem(4, papel);

        return inventory;
    }

    private void buildPag1(){
        criptomonedas.put("BTCUSD", "Bitcoin");
        criptomonedas.put("ETHUSD", "Ethereum");
        criptomonedas.put("LTCUSD", "Litecoin");
    }
}