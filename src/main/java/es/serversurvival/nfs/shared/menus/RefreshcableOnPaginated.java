package es.serversurvival.nfs.shared.menus;

import es.serversurvival.nfs.shared.menus.inventory.InventoryFactory;
import org.bukkit.inventory.Inventory;
import java.util.*;

public interface RefreshcableOnPaginated extends Paginated, Refreshcable{
    InventoryFactory getInventoryFactory();

    void setFactory (InventoryFactory factory);
    void setInventory (Inventory inventory);
    void setPages(List<Page> pages);
}
