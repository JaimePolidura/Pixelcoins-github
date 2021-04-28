package es.serversurvival.shared.menus;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface Clickable {
    void onOherClick(InventoryClickEvent event);
}
