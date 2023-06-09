package es.serversurvival.pixelcoins.jugadores.cambiar.ingresarItem;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class IngresarItemParametros implements ParametrosUseCase {
    @Getter private final UUID jugadorId;
    @Getter private final TipoCambioPixelcoins tipoCambio;
    @Getter private final int cantiadad;

    public static IngresarItemParametros of(UUID jugadorId, TipoCambioPixelcoins tipoCambio, int cantidad) {
        return new IngresarItemParametros(jugadorId, tipoCambio, cantidad);
    }
}
