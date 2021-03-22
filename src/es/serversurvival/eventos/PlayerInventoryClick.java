package es.serversurvival.eventos;

import es.serversurvival.objetos.menus.Clickleable;
import es.serversurvival.objetos.menus.Menu;
import es.serversurvival.objetos.solicitudes.ComprarBolsaSolicitud;
import es.serversurvival.objetos.solicitudes.Solicitud;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PlayerInventoryClick implements Listener {

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent e) throws Exception {
        String inNombre = e.getView().getTitle().toString();
        Player p = (Player) e.getWhoClicked();

        try {
            Menu menu = Menu.getByPlayer(p);
            if(menu!= null){
                e.setCancelled(true);
                if(menu instanceof Clickleable && menu.titulo().equalsIgnoreCase(inNombre)){
                    ((Clickleable) menu).onInventoryClick(e);
                }
            }

            Solicitud solicitud = Solicitud.getByDestinatario(p.getName());
            if(solicitud != null){
                String tituloSol = solicitud.getTitulo();
                if (inNombre.equalsIgnoreCase(tituloSol)) {
                    solicitud = Solicitud.getByDestinatario(p.getName());
                    String nombreItem = null;
                    try {
                        nombreItem = e.getCurrentItem().getType().toString();
                    } catch (NullPointerException ex) {
                        return;
                    }

                    switch (nombreItem) {
                        case "GREEN_WOOL":
                            solicitud.aceptar();
                            break;
                        case "RED_WOOL":
                            solicitud.cancelar();
                            break;
                        default:
                            if (solicitud instanceof ComprarBolsaSolicitud && nombreItem != "AIR") {
                                ComprarBolsaSolicitud comprarAccionSolicitud = (ComprarBolsaSolicitud) solicitud;
                                comprarAccionSolicitud.updateCantidad(e.getCurrentItem());
                                e.setCancelled(true);
                                return;
                            }
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}