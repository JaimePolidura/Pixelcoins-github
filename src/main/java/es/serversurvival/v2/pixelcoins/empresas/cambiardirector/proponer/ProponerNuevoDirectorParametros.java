package es.serversurvival.v2.pixelcoins.empresas.cambiardirector.proponer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class ProponerNuevoDirectorParametros {
    @Getter private final UUID jugadorId;
    @Getter private final UUID empresaId;
    @Getter private final UUID nuevoDirectorId;
    @Getter private final String descripccion;
    @Getter private final long periodoPagoMs;
    @Getter private final double sueldo;
}
