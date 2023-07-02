package es.serversurvival.minecraftserver.jugadores.pagar;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.jugadores.pagar.JugadorPagadoManualmente;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;

import static es.serversurvival._shared.utils.Funciones.formatPixelcoins;
import static org.bukkit.ChatColor.GOLD;

@EventHandler
@AllArgsConstructor
public final class OnPagoRealizado {
    private final EnviadorMensajes enviadorMensajes;
    private final JugadoresService jugadoresService;

    @EventListener
    public void on(JugadorPagadoManualmente e) {
        String pagadorNombre = jugadoresService.getNombreById(e.getJugadorPagadorId());
        String pagadoNombre = jugadoresService.getNombreById(e.getJugadorPagadoId());

        enviadorMensajes.enviarMensaje(e.getJugadorPagadorId(), GOLD + "Has pagado: " + formatPixelcoins(e.getPixelcoins()) + GOLD + "a " + pagadoNombre);
        enviadorMensajes.enviarMensajeYSonido(e.getJugadorPagadoId(), Sound.ENTITY_PLAYER_LEVELUP,  GOLD + pagadorNombre + " te ha pagado " + formatPixelcoins(e.getPixelcoins()));
    }
}
