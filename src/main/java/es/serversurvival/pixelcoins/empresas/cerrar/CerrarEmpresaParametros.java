package es.serversurvival.pixelcoins.empresas.cerrar;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class CerrarEmpresaParametros implements ParametrosUseCase {
    @Getter private final UUID empresaId;
    @Getter private final UUID jugadorId;
}
