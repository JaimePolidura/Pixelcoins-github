package es.serversurvival._shared.menus;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface Clickable {
    void onOherClick(InventoryClickEvent event);
}
