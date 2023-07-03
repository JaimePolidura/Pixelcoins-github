package es.serversurvival.pixelcoins.jugadores.pagar;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public final class JugadorPagadoManualmente extends PixelcoinsEvento implements InvocaAUnReto {
    @Getter private final double pixelcoins;
    @Getter private final UUID jugadorPagadorId;
    @Getter private final UUID jugadorPagadoId;

    @Override
    public Map<UUID, RetoMapping> retosByJugadorId() {
        return Map.of(
                jugadorPagadorId, RetoMapping.JUGADORES_PAGO_PAGADOR,
                jugadorPagadoId, RetoMapping.JUGADORES_PAGO_PAGADO
        );
    }
}
