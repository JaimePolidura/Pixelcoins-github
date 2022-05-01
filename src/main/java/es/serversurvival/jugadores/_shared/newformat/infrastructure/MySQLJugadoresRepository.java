package es.serversurvival.jugadores._shared.newformat.infrastructure;

import es.serversurvival.jugadores._shared.newformat.domain.Jugador;
import es.serversurvival.jugadores._shared.newformat.domain.JugadoresRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class MySQLJugadoresRepository implements JugadoresRepository {
    @Override
    public void save(Jugador jugador) {
    }

    @Override
    public Optional<Jugador> findByJugadorId(UUID jugadorId) {
        return Optional.empty();
    }

    @Override
    public Optional<Jugador> findByNombre(String nombre) {
        return Optional.empty();
    }

    @Override
    public List<Jugador> findAll() {
        return null;
    }
}
