package es.serversurvival.bolsa.activosinfo._shared.application;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfoCacheRepository;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ActivosInfoService {
    private final ActivoInfoCacheRepository respotiroy;

    public ActivosInfoService() {
        this.respotiroy = DependecyContainer.get(ActivoInfoCacheRepository.class);
    }

    public void save(ActivoInfo activoApiCache) {
        this.respotiroy.save(activoApiCache);
    }

    public ActivoInfo getByNombreActivo(String nombreActivo, SupportedTipoActivo supportedTipoActivo) {
        return this.respotiroy.findByNombreActivo(nombreActivo).orElseGet(() -> {
            return getActivoInfoFromAPI(nombreActivo, supportedTipoActivo);
        });
    }

    @SneakyThrows
    private ActivoInfo getActivoInfoFromAPI(String nombreActivo, SupportedTipoActivo supportedTipoActivo) {
        String nombreActivoLargo = supportedTipoActivo.getTipoActivoService().getNombreActivoLargo(nombreActivo);
        double preico = supportedTipoActivo.getTipoActivoService().getPrecio(nombreActivo);

        return new ActivoInfo(nombreActivo, preico, supportedTipoActivo, nombreActivoLargo);
    }

    public List<ActivoInfo> findAll() {
        return this.respotiroy.findAll();
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
        this.respotiroy.deleteByNombreActivo(nombreActivo);
    }

    public boolean existsByNombreActivo(String nombreActivo) {
        return this.respotiroy.findByNombreActivo(nombreActivo).isPresent();
    }
}
