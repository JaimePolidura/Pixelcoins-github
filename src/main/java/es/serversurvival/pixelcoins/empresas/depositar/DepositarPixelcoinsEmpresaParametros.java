package es.serversurvival.pixelcoins.empresas.depositar;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class DepositarPixelcoinsEmpresaParametros implements ParametrosUseCase {
    @Getter private final double pixelcoins;
    @Getter private final UUID empresaId;
    @Getter private final UUID jugadorId;
}
