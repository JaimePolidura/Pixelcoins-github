package es.serversurvival.pixelcoins.config._shared.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationEntry;
import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationKey;
import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public final class ConfigurationService {
    private final Map<ConfigurationKey, ConfigurationEntry> byKeysCache;
    private final ConfigurationRepository repository;

    public ConfigurationService(ConfigurationRepository repository) {
        this.byKeysCache = new ConcurrentHashMap<>();
        this.repository = repository;
    }

    public void save(ConfigurationEntry entry) {
        this.repository.save(entry);
        byKeysCache.put(entry.getKeyy(), entry);
    }

    public List<ConfigurationEntry> findAll() {
        return this.repository.findAll();
    }

    public ConfigurationEntry getByKey(ConfigurationKey key) {
        if(!byKeysCache.containsKey(key)) {
            byKeysCache.put(key, repository.findByKey(key).
                    orElseThrow(() -> new ResourceNotFound("Configuracion "+key+" no encontrada")));
        }

        return byKeysCache.get(key);
    }
}
