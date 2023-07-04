package es.serversurvival.pixelcoins.jugadores.cambiar.ingresarItem;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public final class ItemIngresadoEvento extends PixelcoinsEvento implements InvocaAUnReto {
    @Getter private final UUID jugadorId;
    @Getter private final double pixelcoins;
    @Getter private final TipoCambioPixelcoins tipoCambio;

    @Override
    public Map<UUID, List<RetoMapping>> retosByJugadorId() {
        return Map.of(
                jugadorId, switch (tipoCambio) {
                    case DIAMOND, DIAMOND_BLOCK -> List.of(RetoMapping.JUGADORES_CAMBIO_INGRESAR_DIAMANTE);
                    case QUARTZ_BLOCK -> List.of(RetoMapping.JUGADORES_CAMBIO_INGRESAR_CUARZO);
                    case LAPIS_LAZULI, LAPIS_BLOCK -> List.of(RetoMapping.JUGADORES_CAMBIO_INGRESAR_LAPISLAZULI);
                }
        );
    }
}
