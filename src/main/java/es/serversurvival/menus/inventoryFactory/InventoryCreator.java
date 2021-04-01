package es.serversurvival.menus.inventoryFactory;

import es.serversurvival.util.MinecraftUtils;
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

        inventory.setItem(14, MinecraftUtils.loreDisplayName(GREEN_WOOL, tituloAceptar, loreAceptar));
        inventory.setItem(12, MinecraftUtils.displayname(RED_WOOL, tituloCancel));
        inventory.setItem(15, MinecraftUtils.displayname(LIGHT_GRAY_BANNER, GREEN + "+1"));
        inventory.setItem(16, MinecraftUtils.displayname(LIGHT_GRAY_BANNER, GREEN + "+5"));
        inventory.setItem(17, MinecraftUtils.displayname(LIGHT_GRAY_BANNER, GREEN + "+10"));
        inventory.setItem(11, MinecraftUtils.displayname(LIGHT_GRAY_BANNER, RED + "-1"));
        inventory.setItem(10, MinecraftUtils.displayname(LIGHT_GRAY_BANNER, RED + "-5"));
        inventory.setItem(9, MinecraftUtils.displayname(LIGHT_GRAY_BANNER, RED + "-10"));

        return inventory;
    }

    public static Inventory createSolicitud (String titulo, String nombreItemAceptar, List<String> loreItemAceptar,
                                             String nombreItemCancelar, List<String> loreItemCancelar){
        Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, titulo);

        inventory.setItem(0, MinecraftUtils.loreDisplayName(GREEN_WOOL, nombreItemAceptar, loreItemAceptar));
        inventory.setItem(4, MinecraftUtils.loreDisplayName(RED_WOOL, nombreItemCancelar, loreItemCancelar));

        return inventory;
    }
}
