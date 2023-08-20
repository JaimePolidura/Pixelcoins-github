package es.serversurvival.pixelcoins.config._shared.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public final class ConfigurationEntry {
    @Getter private ConfigurationKey keyy; //Not possible to assign mysql column name, reserved name
    @Getter private String value;

    public ConfigurationEntry editar(String newValue) {
        return new ConfigurationEntry(keyy, newValue);
    }
}
