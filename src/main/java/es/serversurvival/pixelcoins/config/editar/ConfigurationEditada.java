package es.serversurvival.pixelcoins.config.editar;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationKey;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class ConfigurationEditada extends PixelcoinsEvento {
    @Getter private final UUID jugadorId;
    @Getter private final ConfigurationKey key;
    @Getter private final String oldValue;
    @Getter private final String newValue;
}
