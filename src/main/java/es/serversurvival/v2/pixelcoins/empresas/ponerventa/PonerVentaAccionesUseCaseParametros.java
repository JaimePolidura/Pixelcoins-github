package es.serversurvival.v2.pixelcoins.empresas.ponerventa;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class PonerVentaAccionesUseCaseParametros {
    @Getter private final UUID empresaId;
    @Getter private final UUID jugadorId;
    @Getter private final int cantidadAcciones;
    @Getter private final double precioPorAccion;
}
