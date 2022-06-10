package es.serversurvival.bolsa.activosinfo._shared.domain;

import java.util.List;
import java.util.Optional;

public interface ActivoInfoRepository {
    void save(ActivoInfo activoApiCache);

    Optional<ActivoInfo> findByNombreActivo(String nombreActivo);

    List<ActivoInfo> findAll();

    void deleteByNombreActivo(String nombreActivo);
}
