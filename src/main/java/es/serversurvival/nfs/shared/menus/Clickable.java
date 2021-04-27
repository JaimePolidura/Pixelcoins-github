package es.serversurvival.nfs.shared.menus;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface Clickable {
    void onOherClick(InventoryClickEvent event);
}
