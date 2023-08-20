package es.serversurvival.pixelcoins.config.editar;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class EditarConfigurationParametros implements ParametrosUseCase {
    @Getter private final UUID jugadorId;
    @Getter private final ConfigurationKey key;
    @Getter private final String nuevoValor;
}
