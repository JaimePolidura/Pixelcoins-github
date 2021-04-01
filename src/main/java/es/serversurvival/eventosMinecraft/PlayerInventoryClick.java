package es.serversurvival.eventosMinecraft;

import es.serversurvival.menus.Menu;
import es.serversurvival.menus.MenuManager;
import es.serversurvival.menus.menus.CanGoBack;
import es.serversurvival.menus.menus.Clickable;
import es.serversurvival.menus.menus.confirmaciones.AumentoConfirmacion;
import es.serversurvival.menus.menus.confirmaciones.Confirmacion;
import es.serversurvival.menus.menus.Paginated;
import es.serversurvival.menus.menus.solicitudes.Solicitud;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public final class PlayerInventoryClick implements Listener {
    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null || event.getView() == null) return;

        Player jugadorPlayer = (Player) event.getWhoClicked();
        String clickedItemName = event.getCurrentItem().getType().toString();

        Menu menu = MenuManager.getByPlayer(jugadorPlayer.getName());

        if (menu != null) {
            event.setCancelled(true);

            if(menu instanceof Paginated){
                ((Paginated) menu).performClick(event.getCurrentItem().getItemMeta().getDisplayName());
            }

            if(menu instanceof CanGoBack && ((CanGoBack) menu).getNameItemGoBack().equalsIgnoreCase(clickedItemName)) {
                ((CanGoBack) menu).goBack();
            }else if (menu instanceof Clickable) {
                ((Clickable) menu).onOherClick(event);
            }else if(menu instanceof Solicitud){
                ((Solicitud) menu).onOherClick(event);
            }else if(menu instanceof Confirmacion){
                ((Confirmacion) menu).onOherClick(event);

                if(menu instanceof AumentoConfirmacion){
                    ((AumentoConfirmacion) menu).onOherClick(event);
                }
            }
        }
    }
}
