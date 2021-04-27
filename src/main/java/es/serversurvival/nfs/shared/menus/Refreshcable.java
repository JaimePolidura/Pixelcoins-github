package es.serversurvival.nfs.shared.menus;

import org.bukkit.inventory.Inventory;

public interface Refreshcable {
    void setInventory(Inventory inventory);
    void refresh(); //Default: 0
}
