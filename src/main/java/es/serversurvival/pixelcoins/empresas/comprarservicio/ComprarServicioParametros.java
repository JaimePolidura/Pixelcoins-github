package es.serversurvival.pixelcoins.empresas.comprarservicio;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class ComprarServicioParametros implements ParametrosUseCase {
    @Getter private final UUID jugadorId;
    @Getter private final UUID empresaId;
    @Getter private final double pixelcoins;
}
