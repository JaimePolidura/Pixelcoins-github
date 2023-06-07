package es.serversurvival.pixelcoins.deudas.cancelar;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class CancelarDeudaParametros {
    @Getter private final UUID jugadorId;
    @Getter private final UUID deudaId;

    public static CancelarDeudaParametros of(UUID jugadorId, UUID deudaId) {
        return new CancelarDeudaParametros(jugadorId, deudaId);
    }
}
