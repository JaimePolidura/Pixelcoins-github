package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.menus.menus.MateriasPrimasMenu;
import es.serversurvival.util.ItemBuilder;
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

public class MateriasPrimasInventoryFactory extends InventoryFactory {
    private HashMap<String, String> materiasPrimas = new HashMap<>();

    public MateriasPrimasInventoryFactory () {
        materiasPrimas.put("DCOILBRENTEU", "Petroleo (Brent)");
        materiasPrimas.put("DHHNGSP", "Gas natural");
        materiasPrimas.put("DJFUELUSGULF", "Queroseno (Combustible aviones)");
        materiasPrimas.put("GASDESW","Diesel");
    }

    @Override
    protected Inventory buildInventory(String player) {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, MateriasPrimasMenu.titulo);

        Material icono = Material.COAL;

        for (Map.Entry<String, String> entry : materiasPrimas.entrySet()) {
            String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + entry.getValue();

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + "Simbolo: " + entry.getKey());
            lore.add(ChatColor.RED + "Cargando...");

            inventory.addItem(ItemBuilder.loreDisplayName(icono, displayName, lore));
        }

        List<String> infoLore = new ArrayList<>();
        infoLore.add("Para invertir en estas materias primas clickea en cualquiera de ellas y elige la cantidad a comprar");
        infoLore.add("                  ");
        infoLore.add("AVISO: ¡Estas materias primas van con unos 15 minutos aproximadamente de retraso! ");

        inventory.setItem(4, ItemBuilder.loreDisplayName(Material.PAPER, ChatColor.AQUA + "" + ChatColor.BOLD + "INFO", infoLore));

        return inventory;
    }
}
