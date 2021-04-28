package es.serversurvival.shared.menus.inventory;

import es.jaimetruman.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.List;

import static org.bukkit.ChatColor.*;
import static org.bukkit.Material.*;

public class InventoryCreator {

    public static Inventory createInventoryMenu(InventoryFactory factory, String player) {
        return factory.buildInventory(player);
    }

    public static Inventory createConfirmacionAumento (String titulo, String tituloAceptar, List<String> loreAceptar, String tituloCancel) {
        Inventory inventory = Bukkit.createInventory(null, 27, titulo);

        ItemBuilder.of(GREEN_WOOL).title(tituloAceptar).lore(loreAceptar).buildAddInventory(inventory, 14);
        ItemBuilder.of(GREEN_WOOL).title(tituloCancel).buildAddInventory(inventory, 12);
        ItemBuilder.of(LIGHT_GRAY_BANNER).title(GREEN + "+1").buildAddInventory(inventory, 15);
        ItemBuilder.of(LIGHT_GRAY_BANNER).title(GREEN + "+5").buildAddInventory(inventory, 16);
        ItemBuilder.of(LIGHT_GRAY_BANNER).title(GREEN + "+10").buildAddInventory(inventory, 17);
        ItemBuilder.of(LIGHT_GRAY_BANNER).title(RED + "-1").buildAddInventory(inventory, 11);
        ItemBuilder.of(LIGHT_GRAY_BANNER).title(RED + "-5").buildAddInventory(inventory, 10);
        ItemBuilder.of(LIGHT_GRAY_BANNER).title(RED + "-10").buildAddInventory(inventory, 9);

        return inventory;
    }

    public static Inventory createSolicitud (String titulo, String nombreItemAceptar, List<String> loreItemAceptar,
                                             String nombreItemCancelar, List<String> loreItemCancelar){
        Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, titulo);

        ItemBuilder.of(GREEN_WOOL).title(nombreItemAceptar).lore(loreItemAceptar).buildAddInventory(inventory, 0);
        ItemBuilder.of(RED_WOOL).title(nombreItemCancelar).lore(loreItemCancelar).buildAddInventory(inventory, 4);

        return inventory;
    }
}
