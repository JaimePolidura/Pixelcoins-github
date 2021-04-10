package es.serversurvival.menus.inventoryFactory;

import es.jaimetruman.ItemBuilder;
import es.serversurvival.menus.menus.Paginated;
import es.serversurvival.mySQL.*;
import es.serversurvival.util.Funciones;
import es.serversurvival.util.MinecraftUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;

public abstract class InventoryFactory implements AllMySQLTablesInstances {
    protected abstract Inventory buildInventory (String jugador);

    protected ItemStack buildItemGoBack () {
        return ItemBuilder.of(Material.RED_WOOL)
                .title(ChatColor.RED + "" + ChatColor.BOLD + "<--")
                .build();
    }

    protected ItemStack buildItemFordward () {
        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(Paginated.ITEM_NAME_GOFORDWARD)
                .build();
    }
}
