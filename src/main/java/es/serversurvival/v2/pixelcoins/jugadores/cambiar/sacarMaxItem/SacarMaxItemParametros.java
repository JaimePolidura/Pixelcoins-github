package es.serversurvival.v2.pixelcoins.jugadores.cambiar.sacarMaxItem;

import es.serversurvival.v2.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class SacarMaxItemParametros {
    @Getter private final UUID jugadorId;
    @Getter private final TipoCambioPixelcoins tipoCambio;

    public static SacarMaxItemParametros of(UUID jugadorId, TipoCambioPixelcoins tipoCambio) {
        return new SacarMaxItemParametros(jugadorId, tipoCambio);
    }
}
