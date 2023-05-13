package es.serversurvival.bolsa.activosinfo._shared.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.cache.Cache;
import es.serversurvival._shared.cache.UnlimitedCacheSize;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfoRepository;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
public class ActivosInfoService {
    private final ActivoInfoDataService activoInfoDataService;
    private final ActivoInfoRepository respotiroyDb;
    private final Cache<String, ActivoInfo> cache;

    public ActivosInfoService(ActivoInfoDataService activoInfoDataService, ActivoInfoRepository respotiroyDb) {
        this.activoInfoDataService = activoInfoDataService;
        this.respotiroyDb = respotiroyDb;
        this.cache = new UnlimitedCacheSize<>();
    }

    public void save(ActivoInfo activoApiCache) {
        this.cache.put(activoApiCache.getNombreActivo(), activoApiCache);
        this.respotiroyDb.save(activoApiCache);
    }

    public ActivoInfo getByNombreActivo(String nombreActivo, TipoActivo supportedTipoActivo) {
        return this.cache.find(nombreActivo)
                .orElseGet(() -> this.getActivoInfoFromAPI(nombreActivo, supportedTipoActivo)
                        .map(saveToCacheAndDb())
                        .orElseThrow(() -> new ResourceNotFound("Valor no enctrado, solo se pueden los americanos"))
                );
    }

    private Optional<ActivoInfo> getActivoInfoFromAPI(String nombreActivo, TipoActivo supportedTipoActivo) {
        try {
            String nombreActivoLargo = activoInfoDataService.getNombreActivoLargo(supportedTipoActivo, nombreActivo);
            double precio = activoInfoDataService.getPrecio(supportedTipoActivo, nombreActivo);

            return Optional.of(new ActivoInfo(nombreActivo, precio, supportedTipoActivo, nombreActivoLargo));
        } catch (Exception e) {
            e.printStackTrace();

            return Optional.empty();
        }
    }

    public List<ActivoInfo> findAll() {
        return this.cache.all();
    }

    public List<ActivoInfo> findAll(Predicate<? super ActivoInfo> condition){
        return this.findAll().stream()
                .filter(condition)
                .toList();
    }

    public void deleteByNombreActivo(String nombreActivo) {
        this.respotiroyDb.deleteByNombreActivo(nombreActivo);
        this.cache.remove(nombreActivo);
    }

    public boolean existsByNombreActivo(String nombreActivo) {
        return this.cache.find(nombreActivo).isPresent();
    }

    private Function<ActivoInfo, ActivoInfo> saveToCacheAndDb(){
        return activoInfo -> {
            this.cache.put(activoInfo.getNombreActivo(), activoInfo);

            return activoInfo;
        };
    }

    public void initializeCache(){
        this.respotiroyDb.findAll().forEach(activoInfo -> {
            this.cache.put(activoInfo.getNombreActivo(), activoInfo);
        });
    }
}
