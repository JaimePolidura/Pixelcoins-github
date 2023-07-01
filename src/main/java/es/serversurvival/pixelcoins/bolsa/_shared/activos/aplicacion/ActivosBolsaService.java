package es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.application.cache.MUCache;
import es.jaime.javaddd.domain.cache.Cache;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsaRepository;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
public final class ActivosBolsaService {
    private final ActivoBolsaRepository repository;
    private final Cache<UUID, ActivoBolsa> cacheById;

    public ActivosBolsaService(ActivoBolsaRepository repository) {
        this.cacheById = new MUCache<>(50);
        this.repository = repository;
    }

    public void save(ActivoBolsa activoBolsa) {
        repository.save(activoBolsa);
        cacheById.put(activoBolsa.getActivoBolsaId(), activoBolsa);
    }

    public List<ActivoBolsa> findByTipo(TipoActivoBolsa tipo) {
        return repository.findByTipo(tipo);
    }

    public Optional<ActivoBolsa> findById(UUID activoInfoId) {
        var cachedJugador = this.cacheById.find(activoInfoId);

        if(cachedJugador.isEmpty()){
            return repository.findById(activoInfoId)
                    .map(saveActivoToCache());
        }

        return cachedJugador;
    }

    public ActivoBolsa getById(UUID activoInfoId) {
        var cachedActivo = this.cacheById.find(activoInfoId);

        return cachedActivo.orElseGet(() -> this.repository.findById(activoInfoId)
                .map(saveActivoToCache())
                .orElseThrow(() -> new ResourceNotFound("Activo no encontrado")));
    }

    public Optional<ActivoBolsa> findByNombreCortoAndTipoActivo(String nombreCorto, TipoActivoBolsa tipoActivo) {
        return this.repository.findByNombreCortoAndTipoActivo(nombreCorto, tipoActivo);
    }

    public ActivoBolsa getByNombreCortoAndTipoActivo(String nombreCorto, TipoActivoBolsa tipoActivo) {
        return this.repository.findByNombreCortoAndTipoActivo(nombreCorto, tipoActivo)
                .orElseThrow(() -> new ResourceNotFound("Activo no encontrado"));
    }

    private Function<ActivoBolsa, ActivoBolsa> saveActivoToCache(){
        return activoBolsa -> {
            this.cacheById.put(activoBolsa.getActivoBolsaId(), activoBolsa);
            return activoBolsa;
        };
    }
}
