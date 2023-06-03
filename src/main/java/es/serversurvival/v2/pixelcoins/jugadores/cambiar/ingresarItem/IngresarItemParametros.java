package es.serversurvival.v2.pixelcoins.jugadores.cambiar.ingresarItem;

import es.serversurvival.v2.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class IngresarItemParametros {
    @Getter private final UUID jugadorId;
    @Getter private final TipoCambioPixelcoins tipoCambio;
    @Getter private final int cantiadad;

    public static IngresarItemParametros of(UUID jugadorId, TipoCambioPixelcoins tipoCambio, int cantidad) {
        return new IngresarItemParametros(jugadorId, tipoCambio, cantidad);
    }
}
