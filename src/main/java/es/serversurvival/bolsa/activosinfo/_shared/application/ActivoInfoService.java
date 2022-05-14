package es.serversurvival.bolsa.activosinfo._shared.application;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfoCacheRepository;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class ActivoInfoService {
    private final ActivoInfoCacheRepository repositoryCache;

    public ActivoInfoService() {
        this.repositoryCache = DependecyContainer.get(ActivoInfoCacheRepository.class);
    }

    public void save(ActivoInfo activoApiCache) {
        this.repositoryCache.save(activoApiCache);
    }

    public ActivoInfo getByNombreActivo(String nombreActivo, SupportedTipoActivo supportedTipoActivo) {
        return this.repositoryCache.findByNombreActivo(nombreActivo).orElseGet(() -> {
            String nombreActivoLargo = supportedTipoActivo.getTipoActivoService().getNombreActivoLargo(nombreActivo);
            double preico = supportedTipoActivo.getTipoActivoService().getPrecio(nombreActivo);

            return new ActivoInfo(nombreActivo, preico, supportedTipoActivo, nombreActivoLargo);
        });
    }

    public List<ActivoInfo> findAll() {
        return this.repositoryCache.findAll();
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
        this.repositoryCache.deleteByNombreActivo(nombreActivo);
    }

    public boolean existsByNombreActivo(String nombreActivo) {
        return this.repositoryCache.existsByNombreActivo(nombreActivo);
    }
}
