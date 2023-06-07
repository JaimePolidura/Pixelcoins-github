package es.serversurvival.pixelcoins.empresas.ponerventa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class PonerVentaAccionesParametros {
    @Getter private final UUID empresaId;
    @Getter private final UUID jugadorId;
    @Getter private final int cantidadAcciones;
    @Getter private final double precioPorAccion;
}
