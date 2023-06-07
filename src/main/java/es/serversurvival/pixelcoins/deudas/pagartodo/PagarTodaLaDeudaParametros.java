package es.serversurvival.pixelcoins.deudas.pagartodo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class PagarTodaLaDeudaParametros {
    @Getter private final UUID deudaId;
    @Getter private final UUID jugadorId;

    public static PagarTodaLaDeudaParametros of(UUID deudaId, UUID jugadorId) {
        return new PagarTodaLaDeudaParametros(deudaId, jugadorId)
    }
}
