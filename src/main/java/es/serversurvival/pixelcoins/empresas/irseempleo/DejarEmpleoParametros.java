package es.serversurvival.pixelcoins.empresas.irseempleo;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class DejarEmpleoParametros implements ParametrosUseCase {
    @Getter private final UUID jugadorId;
    @Getter private final UUID empresaId;
}
