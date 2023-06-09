package es.serversurvival.pixelcoins.empresas.cambiardirector.proponer;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class ProponerNuevoDirectorParametros implements ParametrosUseCase {
    @Getter private final UUID jugadorId;
    @Getter private final UUID empresaId;
    @Getter private final UUID nuevoDirectorId;
    @Getter private final String descripccion;
    @Getter private final long periodoPagoMs;
    @Getter private final double sueldo;
}
