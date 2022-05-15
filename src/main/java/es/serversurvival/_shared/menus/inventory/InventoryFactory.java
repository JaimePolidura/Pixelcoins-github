package es.serversurvival._shared.menus.inventory;

import es.jaimetruman.ItemBuilder;
import es.serversurvival._shared.menus.Paginated;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
