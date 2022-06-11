package es.serversurvival.bolsa.activosinfo._shared.application;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.cache.Cache;
import es.serversurvival._shared.cache.UnlimitedCacheSize;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfoRepository;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ActivosInfoService {
    private final ActivoInfoRepository respotiroyDb;
    private final Cache<String, ActivoInfo> cache;

    public ActivosInfoService() {
        this.cache = new UnlimitedCacheSize<>();
        this.respotiroyDb = DependecyContainer.get(ActivoInfoRepository.class);
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
            String nombreActivoLargo = supportedTipoActivo.getTipoActivoService().getNombreActivoLargo(nombreActivo);
            double precio = supportedTipoActivo.getTipoActivoService().getPrecio(nombreActivo);

            return Optional.of(new ActivoInfo(nombreActivo, precio, supportedTipoActivo, nombreActivoLargo));
        } catch (Exception e) {
            e.printStackTrace();

            return Optional.empty();
        }
    }

    public List<ActivoInfo> findAll() {
        return this.cache.all();
    }

    public Map<String, ActivoInfo> findAllToMap(){
        return this.findAll().stream()
                .collect(Collectors.toMap(ActivoInfo::getNombreActivo, activoInfo -> activoInfo));
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
