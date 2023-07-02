package es.serversurvival.minecraftserver.deudas.emitir;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.deudas.emitir.DeudaEmitida;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;

import java.util.UUID;

import static es.serversurvival.minecraftserver._shared.MinecraftUtils.broadcastExcept;
import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.GOLD;

@Service
@AllArgsConstructor
public final class OnDeudaEmitida {
    private final EnviadorMensajes enviadorMensajes;
    private final JugadoresService jugadoresService;

    @EventListener
    public void on(DeudaEmitida emitida) {
        UUID emisorJugadorId = emitida.getDeudaEmitidaParametros().getJugadorId();
        double pixelcoins = emitida.getDeudaEmitidaParametros().getNominal();

        enviadorMensajes.enviarMensajeYSonido(emisorJugadorId, Sound.ENTITY_PLAYER_LEVELUP, GOLD + "Has puesto la deuda en el mercado. por" + pixelcoins +
                AQUA + "/deudas mercado");
        broadcastExcept(emisorJugadorId, GOLD + jugadoresService.getNombreById(emisorJugadorId) + " ha puesto deuda en el mercado. "
                + AQUA + "/deudas mercado");
    }
}
