package es.serversurvival.eventosMinecraft;

import es.serversurvival.menus.Menu;
import es.serversurvival.menus.MenuManager;
import es.serversurvival.menus.menus.solicitudes.Solicitud;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public final class PlayerCloseInventory implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        String titulo = event.getView().getTitle();
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
