package es.serversurvival.pixelcoins.jugadores._shared.estadisticas;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JugadoresEstadisticasRepository {
    void save(JugadorEstadisticas estadisticas);

    Optional<JugadorEstadisticas> findById(UUID jugadorId);

    List<JugadorEstadisticas> sortBy(JugadorTipoContadorEstadistica tipoContadorEstadistica, boolean creciente);
}
