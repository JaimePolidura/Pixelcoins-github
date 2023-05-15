package es.serversurvival.v2.pixelcoins.jugadores._shared;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JugadoresRepository {
    void save(Jugador jugador);

    Optional<Jugador> findById(UUID jugadorId);

    Optional<Jugador> findByNombre(String nombre);

    List<Jugador> findAll();
}
