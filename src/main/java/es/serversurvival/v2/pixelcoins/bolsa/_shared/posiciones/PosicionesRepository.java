package es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PosicionesRepository {
    void save(Posicion posicion);

    List<Posicion> findByJugadorId(UUID jugadorId);

    Optional<Posicion> findById(UUID posicionId);

    void deleteById(UUID posicionId);

    List<Posicion> findPosicionesCerradasSortByRentabilidad();
}
