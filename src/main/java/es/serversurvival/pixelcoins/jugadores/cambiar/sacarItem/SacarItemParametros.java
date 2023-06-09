package es.serversurvival.pixelcoins.jugadores.cambiar.sacarItem;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import es.serversurvival.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class SacarItemParametros implements ParametrosUseCase {
    @Getter private final UUID jugadorId;
    @Getter private final TipoCambioPixelcoins tipoCambio;
    @Getter private final int cantidad;

    public static SacarItemParametros of(UUID jugadorId, TipoCambioPixelcoins tipoCambio, int cantidad) {
        return new SacarItemParametros(jugadorId, tipoCambio, cantidad);
    }
}
