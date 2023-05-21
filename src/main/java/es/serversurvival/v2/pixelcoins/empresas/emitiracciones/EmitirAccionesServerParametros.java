package es.serversurvival.v2.pixelcoins.empresas.emitiracciones;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class EmitirAccionesServerParametros {
    @Getter private final UUID empresaId;
    @Getter private final UUID jugadorId;
    @Getter private final int numeroNuevasAcciones;
    @Getter private final double precioPorAccoin;
}
