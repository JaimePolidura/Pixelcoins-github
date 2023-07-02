package es.serversurvival.minecraftserver.deudas.pagartodo;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasService;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.deudas.pagartodo.DeudaPagadoPorCompleto;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;

import static es.serversurvival._shared.utils.Funciones.formatPixelcoins;
import static org.bukkit.ChatColor.GOLD;

@EventHandler
@AllArgsConstructor
public final class OnDeudaPagadoCompleto {
    private final EnviadorMensajes enviadorMensajes;
    private final JugadoresService jugadoresService;
    private final DeudasService deudasService;

    @EventListener
    public void on(DeudaPagadoPorCompleto e) {
        Deuda deuda = deudasService.getById(e.getDeudaId());
        String nombreDeudor = jugadoresService.getNombreById(deuda.getAcredorJugadorId());

        enviadorMensajes.enviarMensaje(e.getDeudorJugadorId(), GOLD + "Has pagado toda la deuda. En total han sido " +
                formatPixelcoins(- e.getTotalCoste()));

        enviadorMensajes.enviarMensajeYSonido(deuda.getAcredorJugadorId(), Sound.ENTITY_PLAYER_LEVELUP, nombreDeudor +
                " te ha pagado toda la deuda de " + formatPixelcoins(e.getTotalCoste()));
    }
}
