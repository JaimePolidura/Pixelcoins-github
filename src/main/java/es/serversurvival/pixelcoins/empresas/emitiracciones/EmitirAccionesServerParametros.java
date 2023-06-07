package es.serversurvival.pixelcoins.empresas.emitiracciones;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class EmitirAccionesServerParametros {
    @Getter private final UUID empresaId;
    @Getter private final UUID jugadorId;
    @Getter private final int numeroNuevasAcciones;
    @Getter private final double precioPorAccion;
}
