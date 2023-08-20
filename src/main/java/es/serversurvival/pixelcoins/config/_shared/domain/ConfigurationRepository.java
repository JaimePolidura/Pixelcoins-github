package es.serversurvival.pixelcoins.config._shared.domain;

import java.util.List;
import java.util.Optional;

public interface ConfigurationRepository {
    void save(ConfigurationEntry entry);

    Optional<ConfigurationEntry> findByKey(ConfigurationKey key);

    List<ConfigurationEntry> findAll();
}
