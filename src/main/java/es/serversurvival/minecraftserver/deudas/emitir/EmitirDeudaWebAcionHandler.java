package es.serversurvival.minecraftserver.deudas.emitir;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.minecraftserver.webaction.WebActionException;
import es.serversurvival.minecraftserver.webaction.WebActionHandler;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.deudas.emitir.EmitirDeudaParametros;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;

import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival.minecraftserver._shared.MinecraftUtils.*;
import static org.bukkit.ChatColor.*;

@Service
@AllArgsConstructor
public final class EmitirDeudaWebAcionHandler implements WebActionHandler<EmitirDeudaWebActionRequestBody> {
    private final JugadoresService jugadoresService;
    private final EnviadorMensajes enviadorMensajes;
    private final UseCaseBus useCaseBus;

    @Override
    public void handle(UUID jugadorId, EmitirDeudaWebActionRequestBody body) throws WebActionException {
        useCaseBus.handle(EmitirDeudaParametros.builder()
                .nominal(body.getPixelcoins())
                .interes(body.getInteres() / 100)
                .jugadorId(jugadorId)
                .numeroCuotasTotales(body.getNumeroCuotasTotales())
                .periodoPagoCuota(diasToMillis(body.getPeriodoPagoCuotaEnDias()))
                .build());

        enviadorMensajes.enviarMensajeYSonido(jugadorId, Sound.ENTITY_PLAYER_LEVELUP, GOLD + "Has puesto la deuda en el mercado. por" + formatPixelcoins(body.getPixelcoins()) +
                AQUA + "/deudas mercado");
        broadcastExcept(jugadorId, GOLD + jugadoresService.getNombreById(jugadorId) + " ha puesto deuda en el mercado. "
                + AQUA + "/deudas mercado");
    }
}
