package es.serversurvival.v2.pixelcoins.empresas.emitir;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class EmitirAccionesServerUseCaseParametros {
    @Getter private final UUID empresaId;
    @Getter private final UUID jugadorId;
    @Getter private final int numeroNuevasAcciones;
    @Getter private final double precioPorAccoin;
}
