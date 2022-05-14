package es.serversurvival.bolsa.activosinfo._shared.domain;

import java.util.List;
import java.util.Optional;

public interface ActivoInfoCacheRepository {
    void save(ActivoInfo activoApiCache);

    Optional<ActivoInfo> findByNombreActivo(String nombreActivo);

    List<ActivoInfo> findAll();

    boolean existsByNombreActivo(String nombreActivo);

    void deleteByNombreActivo(String nombreActivo);
}
