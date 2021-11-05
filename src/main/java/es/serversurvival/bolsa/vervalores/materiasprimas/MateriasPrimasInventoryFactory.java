package es.serversurvival.bolsa.vervalores.materiasprimas;

import es.jaimetruman.ItemBuilder;
import es.serversurvival.shared.menus.inventory.InventoryFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

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

            ItemBuilder.of(icono).title(displayName).lore(lore).buildAddInventory(inventory);
        }

        List<String> infoLore = new ArrayList<>();
        infoLore.add("Para invertir en estas materias primas clickea en cualquiera de ellas y elige la cantidad a comprar");
        infoLore.add("                  ");
        infoLore.add("AVISO: ¡Estas materias primas van con unos 15 minutos aproximadamente de retraso! ");

        ItemBuilder.of(Material.PAPER).title(ChatColor.AQUA + "" + ChatColor.BOLD + "INFO").lore(infoLore).buildAddInventory(inventory, 4);

        return inventory;
    }
}
