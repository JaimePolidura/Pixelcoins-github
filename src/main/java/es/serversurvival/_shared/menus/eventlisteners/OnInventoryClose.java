package es.serversurvival._shared.menus.eventlisteners;

import es.serversurvival._shared.menus.Menu;
import es.serversurvival._shared.menus.MenuManager;
import es.serversurvival._shared.menus.solicitudes.Solicitud;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public final class OnInventoryClose implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        String jugador = event.getPlayer().getName();

        Menu menu = MenuManager.getByPlayer(jugador);
        if(menu != null){
            if(menu instanceof Solicitud){
                ((Solicitud) menu).onCloseInventory(event);
            }else{
                MenuManager.borrarMenu(jugador);
            }
        }
    }
}
