package es.serversurvival.menus.inventoryFactory.inventories;

import es.jaimetruman.ItemBuilder;
import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.menus.menus.CriptomonedasMenu;
import es.serversurvival.util.MinecraftUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CriptomonedasInventoryFactory extends InventoryFactory {
    private final Map<String, String> criptomonedas = new HashMap<>();

    public CriptomonedasInventoryFactory() {
        criptomonedas.put("BTCUSD", "Bitcoin");
        criptomonedas.put("ETHUSD", "Ethereum");
        criptomonedas.put("LTCUSD", "Litecoin");
    }

    @Override
    protected Inventory buildInventory(String player) {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, CriptomonedasMenu.titulo);

        for (Map.Entry<String, String> entry : criptomonedas.entrySet()) {
            List<String> lore = new ArrayList<>();
            String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + entry.getValue();
            lore.add(ChatColor.GOLD + "Simbolo: " + entry.getKey());
            lore.add(ChatColor.RED + "Cargando...");

            ItemBuilder.of(Material.GOLD_BLOCK).title(displayName).lore(lore).buildAddInventory(inventory);
        }

        List<String> infoLore = new ArrayList<>();
        infoLore.add("Para invertir en estas criptomonedas clickea en cualquiera de ellas y elige la cantidad a comprar");
        infoLore.add("                  ");
        infoLore.add("AVISO: ¡Estas criptomonedas van con unos 15 minutos aproximadamente de retraso! ");

        String displayName = ChatColor.AQUA + "" + ChatColor.BOLD + "INFO";

        ItemBuilder.of(Material.PAPER).title(displayName).lore(infoLore).buildAddInventory(inventory, 4);

        return inventory;
    }
}
