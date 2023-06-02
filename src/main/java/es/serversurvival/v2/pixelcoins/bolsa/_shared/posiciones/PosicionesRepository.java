package es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones;

import java.util.Optional;
import java.util.UUID;

public interface PosicionesRepository {
    void save(Posicion posicion);

    Optional<Posicion> findById(UUID posicionId);

    void deleteById(UUID posicionId);
}
