package es.serversurvival.jugadores._shared.newformat.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JugadoresRepository {
    void save(Jugador jugador);

    Optional<Jugador> findById(UUID jugadorId);

    Optional<Jugador> findByNombre(String nombre);

    List<Jugador> findAll();
}
