package es.serversurvival.menus.menus.confirmaciones;

import es.serversurvival.menus.menus.Clickable;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface Confirmacion extends Clickable {
    void confirmar();
    void cancelar();

    @Override
    default void onOherClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null) return;

        String nombreItem = event.getCurrentItem().getType().toString();
        switch (nombreItem){
            case "GREEN_WOOL":
                confirmar();
                break;
            case "RED_WOOL":
                cancelar();
                break;
            default:
                onOtherClick(event);
        }
    }

    void onOtherClick (InventoryClickEvent event);
}
