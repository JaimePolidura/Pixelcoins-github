package es.serversurvival.v2.pixelcoins.deudas.cancelar;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class CancelarDeudaParametros {
    @Getter private final UUID jugadorId;
    @Getter private final UUID deudaId;
}
