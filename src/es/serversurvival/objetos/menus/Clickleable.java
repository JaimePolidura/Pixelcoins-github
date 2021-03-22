package es.serversurvival.objetos.menus;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface Clickleable {
    void onInventoryClick(InventoryClickEvent event);
}