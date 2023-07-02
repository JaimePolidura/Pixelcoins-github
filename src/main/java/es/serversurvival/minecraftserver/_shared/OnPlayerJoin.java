package es.serversurvival.minecraftserver._shared;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.serversurvival.pixelcoins.mensajes.verpendientes.LeectorMensajesService;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static org.bukkit.ChatColor.*;

@EventHandler
@RequiredArgsConstructor
public final class OnPlayerJoin  implements Listener {
    private final LeectorMensajesService leectorMensajesService;

    @org.bukkit.event.EventHandler
    public void on(PlayerJoinEvent event) {
        int nMensajesPendientes = leectorMensajesService.getNumeroMensajesPendientes(event.getPlayer().getUniqueId());

        if(nMensajesPendientes == 1) {
            event.getPlayer().sendMessage(GOLD + "Tienes "+ DARK_AQUA+"1"+GOLD+" mensaje pendientes: "+AQUA+"/mensajes");
        }else if(nMensajesPendientes > 1) {
            event.getPlayer().sendMessage(GOLD + "Tienes "+ DARK_AQUA+nMensajesPendientes+GOLD+" mensajes pendientes: "+AQUA+"/mensajes");
        }else {
            event.getPlayer().sendMessage(GOLD + "No tienes mensajes pendientes");
        }
    }
}
