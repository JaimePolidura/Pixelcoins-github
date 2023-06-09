package es.serversurvival.pixelcoins.mensajes._shared.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MensajesRepository {
    void save(Mensaje mensaje);

    List<Mensaje> findByJugadorIdNoVistos(UUID jugadorId);

    void deleteByFechaVistaLessThan(LocalDateTime lessThan);
}
