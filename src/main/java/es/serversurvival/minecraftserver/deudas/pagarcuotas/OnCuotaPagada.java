package es.serversurvival.minecraftserver.deudas.pagarcuotas;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasService;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.deudas.pagarcuotas.CuotaPagada;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.Sound.*;

@EventHandler
@AllArgsConstructor
public final class OnCuotaPagada {
    private final JugadoresService jugadoresService;
    private final EnviadorMensajes enviadorMensajes;
    private final DeudasService deudasService;

    @EventListener
    public void on(CuotaPagada e) {
        Deuda deuda = deudasService.getById(e.getDeudaId());
        String acredorNombre = jugadoresService.getNombreById(deuda.getAcredorJugadorId());
        String deudorNombre = jugadoresService.getNombreById(deuda.getDeudorJugadorId());

        enviadorMensajes.enviarMensajeYSonido(deuda.getAcredorJugadorId(), ENTITY_PLAYER_LEVELUP, GOLD + deudorNombre +
                " te ha pagado la cuota " + formatPixelcoins(e.getCuota()) + "de la deuda que tiene contigo");
        enviadorMensajes.enviarMensaje(deuda.getAcredorJugadorId(), GOLD + "Has pagado la cuota " + formatPixelcoins(e.getCuota())
                + " de la deuda que tienes con " + acredorNombre);
    }
}
