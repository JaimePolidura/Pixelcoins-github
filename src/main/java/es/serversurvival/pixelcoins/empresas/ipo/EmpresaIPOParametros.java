package es.serversurvival.pixelcoins.empresas.ipo;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class EmpresaIPOParametros implements ParametrosUseCase {
    @Getter private final UUID jugadorId;
    @Getter private final UUID empresaId;
    @Getter private final double precioPorAccion;
    @Getter private final int numeroAccionesVender;
}
