package es.serversurvival.bolsa.activosinfo._shared.infrastructure;

import es.serversurvival._shared.cache.Cache;
import es.serversurvival._shared.cache.UnlimitedCacheSize;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfoRepository;

import java.util.List;
import java.util.Optional;

public final class InMemoryActivoInfoCacheRepository implements ActivoInfoRepository {
    private final Cache<String, ActivoInfo> cache;

    public InMemoryActivoInfoCacheRepository() {
        this.cache = new UnlimitedCacheSize<>();
    }

    @Override
    public void save(ActivoInfo activoApiCache) {
        this.cache.put(activoApiCache.getNombreActivo(), activoApiCache);
    }

    @Override
    public Optional<ActivoInfo> findByNombreActivo(String nombreActivo) {
        return this.cache.find(nombreActivo);
    }

    @Override
    public List<ActivoInfo> findAll() {
        return this.cache.all();
    }

    @Override
    public void deleteByNombreActivo(String nombreActivo) {
        cache.remove(nombreActivo);
    }
}
