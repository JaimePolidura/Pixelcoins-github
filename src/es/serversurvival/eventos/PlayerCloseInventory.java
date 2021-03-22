package es.serversurvival.eventos;

import es.serversurvival.objetos.solicitudes.Solicitud;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class PlayerCloseInventory implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        String titulo = e.getView().getTitle();
        String destinatario = ((Player) e.getPlayer()).getName();
        Solicitud solicitud = Solicitud.getByDestinatario(destinatario);
        String solicitudTitulo = null;

        try {
            solicitudTitulo = Solicitud.getByDestinatario(destinatario).getTitulo();
        } catch (NullPointerException ex) {
            return;
        }

        if (solicitudTitulo.equalsIgnoreCase(titulo)) {
            solicitud.cancelar();
        }
    }
}