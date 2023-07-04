package es.serversurvival.pixelcoins.retos._shared.retosadquiridos.domain;

import java.util.Optional;
import java.util.UUID;

public interface RetosAdquiridosRepository {
    void save(RetoAdquirido retoAdquirido);

    Optional<RetoAdquirido> findByJugadorIdAndRetoId(UUID jugadorId, UUID retoId);
}
