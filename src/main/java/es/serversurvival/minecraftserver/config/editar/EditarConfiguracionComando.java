package es.serversurvival.minecraftserver.config.editar;

import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationKey;
import lombok.Getter;

public final class EditarConfiguracionComando {
    @Getter private ConfigurationKey key;
    @Getter private String nuevoValor;
}
