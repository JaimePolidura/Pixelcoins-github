package es.serversurvival.legacy.menus.menus;

import es.serversurvival.legacy.menus.inventoryFactory.InventoryFactory;
import org.bukkit.inventory.Inventory;
import java.util.*;

public interface RefreshcableOnPaginated extends Paginated, Refreshcable{
    InventoryFactory getInventoryFactory();

    void setFactory (InventoryFactory factory);
    void setInventory (Inventory inventory);
    void setPages(List<Page> pages);
}
