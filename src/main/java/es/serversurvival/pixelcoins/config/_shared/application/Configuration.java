package es.serversurvival.pixelcoins.config._shared.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationKey;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class Configuration {
    private final ConfigurationService configurationService;

    public String get(ConfigurationKey key) {
        return this.configurationService.getByKey(key)
                .getValue();
    }

    public double getDouble(ConfigurationKey key) {
        return Double.parseDouble(configurationService.getByKey(key)
                .getValue());
    }

    public int getInt(ConfigurationKey key) {
        return Integer.parseInt(configurationService.getByKey(key)
                .getValue());
    }

    public long getLong(ConfigurationKey key) {
        return Long.parseLong(configurationService.getByKey(key)
                .getValue());
    }

    public boolean getBoolean(ConfigurationKey key) {
        return Boolean.parseBoolean(configurationService.getByKey(key)
                .getValue());
    }
}
