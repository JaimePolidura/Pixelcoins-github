package es.serversurvival._shared.menus;

import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;


public abstract class Menu implements AllMySQLTablesInstances {
    public abstract Inventory getInventory();
    public abstract Player getPlayer();

    public void openMenu(){
        getPlayer().openInventory(getInventory());
        MenuManager.nuevoMenu(getPlayer().getName(), this);
    }

    public void closeMenu(){
        MenuManager.borrarMenu(getPlayer().getName());
        getPlayer().closeInventory();
    }
}
