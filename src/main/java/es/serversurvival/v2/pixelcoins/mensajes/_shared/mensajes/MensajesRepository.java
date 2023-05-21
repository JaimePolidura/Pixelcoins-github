package es.serversurvival.v2.pixelcoins.mensajes._shared.mensajes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MensajesRepository {
    void save(Mensaje mensaje);

    List<Mensaje> findByJugadorIdNoVistos(UUID jugadorId);

    void deleteByFechaVistaLessThan(LocalDateTime lessThan);
}
