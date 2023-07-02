package es.serversurvival.minecraftserver.deudas.pagarcuotas;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasService;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.deudas.pagarcuotas.CuotaNoPagada;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@EventHandler
@AllArgsConstructor
public final class OnCuotaNoPagada {
    private final JugadoresService jugadoresService;
    private final EnviadorMensajes enviadorMensajes;
    private final DeudasService deudasService;

    @EventListener
    public void on(CuotaNoPagada e) {
        Deuda deuda = deudasService.getById(e.getDeudaId());
        String acredorNombre = jugadoresService.getNombreById(deuda.getAcredorJugadorId());
        String deudorNombre = jugadoresService.getNombreById(deuda.getDeudorJugadorId());

        enviadorMensajes.enviarMensajeYSonido(e.getDeudorJugadorId(), Sound.BLOCK_ANVIL_LAND, RED + "No has podido pagar la cuota de "+
                formatPixelcoins(e.getCuota()) + RED+ "de la deuda que tienes con " + acredorNombre);
        enviadorMensajes.enviarMensajeYSonido(e.getDeudorJugadorId(), Sound.BLOCK_ANVIL_LAND, RED + deudorNombre + " no has podido pagar la cuota de "+
                formatPixelcoins(e.getCuota()) + RED+ "de la deuda que tiene contigo");
    }
}
