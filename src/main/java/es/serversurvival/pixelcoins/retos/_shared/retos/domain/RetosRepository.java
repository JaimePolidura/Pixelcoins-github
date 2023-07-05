package es.serversurvival.pixelcoins.retos._shared.retos.domain;

import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.Reto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RetosRepository {
    void save(Reto reto);

    Optional<Reto> findById(int retoId);

    Optional<Reto> findByMapping(RetoMapping retoMapping);

    List<Reto> findByRetoLineaPadre(UUID retoLineaPadreId);

    List<Reto> findAll();
}
