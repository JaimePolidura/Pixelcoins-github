package es.serversurvival.v2.pixelcoins.empresas.ipo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class EmpresaIPOParametros {
    @Getter private final UUID jugadorId;
    @Getter private final UUID empresaId;
    @Getter private final double precioPorAccion;
    @Getter private final int numeroAccionesVender;
}
