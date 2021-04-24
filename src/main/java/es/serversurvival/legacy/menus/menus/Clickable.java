package es.serversurvival.legacy.menus.menus;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface Clickable {
    void onOherClick(InventoryClickEvent event);
}
