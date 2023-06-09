package es.serversurvival.pixelcoins.empresas.contratar;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class ContratarEmpleadoParametros implements ParametrosUseCase {
    @Getter private final UUID jugadorIdContrador;
    @Getter private final UUID jugadorIdAContratar;
    @Getter private final UUID empresaId;
    @Getter private final String descripccion;
    @Getter private final double sueldo;
    @Getter private final long periodoPagoMs;
}
