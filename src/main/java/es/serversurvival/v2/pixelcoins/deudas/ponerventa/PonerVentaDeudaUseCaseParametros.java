package es.serversurvival.v2.pixelcoins.deudas.ponerventa;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class PonerVentaDeudaUseCaseParametros {
    @Getter private final UUID jugadorId;
    @Getter private final UUID deudaId;
    @Getter private final double precio;
}
