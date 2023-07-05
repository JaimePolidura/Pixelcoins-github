package es.serversurvival.pixelcoins.retos._shared.retos.domain;

import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RetosRepository {
    void save(Reto reto);

    Optional<Reto> findById(UUID retoId);

    Optional<Reto> findByMapping(RetoMapping retoMapping);

    List<Reto> findByModuloAndRetoPadreId(ModuloReto modulo, UUID retoPadreId);

    List<Reto> findByRetoPadreProgresionIdSortByPosicion(UUID retoLineaPadreId);

    List<Reto> findAll();
}
