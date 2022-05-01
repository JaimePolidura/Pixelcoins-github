package es.serversurvival.jugadores._shared.newformat.application;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.cache.Cache;
import es.serversurvival._shared.cache.LRUCache;
import es.serversurvival.jugadores._shared.newformat.domain.Jugador;
import es.serversurvival.jugadores._shared.newformat.domain.JugadoresRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class JugadoresRepositoryService {
    private final Cache<String, Jugador> jugadoresCache;
    private final JugadoresRepository jugadoresRepository;

    public JugadoresRepositoryService() {
        this.jugadoresCache = new LRUCache<>(50);
        this.jugadoresRepository = DependecyContainer.get(JugadoresRepository.class);
    }

    public void save(Jugador jugador) {
        this.jugadoresRepository.save(jugador);
        this.jugadoresCache.add(jugador.getNombre(), jugador);
    }

    public Optional<Jugador> findByJugadorId(UUID jugadorId) {
        var jugadorOptional = this.jugadoresRepository.findByJugadorId(jugadorId);

        jugadorOptional.ifPresent(jugador -> this.jugadoresCache.add(jugador.getNombre(), jugador));

        return jugadorOptional;
    }

    public Optional<Jugador> findByNombre(String nombre) {
        var cacheResult = this.jugadoresCache.get(nombre);

        return cacheResult.isPresent() ?
                cacheResult :
                this.jugadoresRepository.findByNombre(nombre).map(jugador -> {
                    this.jugadoresCache.add(jugador.getNombre(), jugador);

                    return jugador;
                });
    }

    public List<Jugador> findAll() {
        return this.jugadoresRepository.findAll();
    }
}
