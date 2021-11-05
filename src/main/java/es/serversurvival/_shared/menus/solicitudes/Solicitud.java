package es.serversurvival._shared.menus.solicitudes;

import es.serversurvival._shared.menus.Clickable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public interface Solicitud extends Clickable {
    String getDestinatario();
    void enviarSolicitud();
    void aceptar();
    void cancelar();
    void onCloseInventory(InventoryCloseEvent event);

    @Override
    default void onOherClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null) return;

        String nombreItem = event.getCurrentItem().getType().toString();
        switch (nombreItem){
            case "GREEN_WOOL":
                aceptar();
                break;
            case "RED_WOOL":
                cancelar();
                break;
        }
    }
}
