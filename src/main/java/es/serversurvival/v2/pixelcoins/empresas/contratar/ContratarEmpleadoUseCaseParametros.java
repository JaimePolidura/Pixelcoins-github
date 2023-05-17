package es.serversurvival.v2.pixelcoins.empresas.contratar;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class ContratarEmpleadoUseCaseParametros {
    @Getter private final UUID jugadorIdContrador;
    @Getter private final UUID jugadorIdAContratar;
    @Getter private final UUID empresaId;
    @Getter private final String descripccion;
    @Getter private final double sueldo;
    @Getter private final long periodoPagoMs;
}
