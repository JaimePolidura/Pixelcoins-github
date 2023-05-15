package es.serversurvival.v2.pixelcoins.jugadores._shared;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.application.cache.MUCache;
import es.jaime.javaddd.domain.cache.Cache;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
public final class JugadoresService {
    private final JugadoresRepository jugadoresRepository;
    private final Cache<String, Jugador> cacheByNombre;
    private final Cache<UUID, Jugador> cacheById;

    public JugadoresService(JugadoresRepository jugadoresRepository) {
        this.jugadoresRepository = jugadoresRepository;
        this.cacheById = new MUCache<>(50);
        this.cacheByNombre = new MUCache<>(50);
    }

    public void save(Jugador jugador){
        this.jugadoresRepository.save(jugador);
        this.cacheById.put(jugador.getJugadorId(), jugador);
        this.cacheByNombre.put(jugador.getNombre(), jugador);
    }
    
    public Jugador getByNombre(String nombre){
        return this.cacheByNombre.find(nombre).orElseGet(() -> this.jugadoresRepository.findByNombre(nombre)
                .map(saveJugadorToCache())
                .orElseThrow(() -> new ResourceNotFound("Jugador no encontrado")));
    }

    public Jugador getById(UUID jugadorId){
        var cachedJugador = this.cacheById.find(jugadorId);

        return cachedJugador.orElseGet(() -> this.jugadoresRepository.findById(jugadorId)
                .map(saveJugadorToCache())
                .orElseThrow(() -> new ResourceNotFound("Jugador no encontrado")));
    }

    public Optional<Jugador> findByNombre(String nombre){
        return jugadoresRepository.findByNombre(nombre);
    }

    public Optional<Jugador> findById(UUID jugadorId){
        return jugadoresRepository.findById(jugadorId);
    }

    public List<Jugador> findAll(){
        return this.jugadoresRepository.findAll().stream()
                .map(saveJugadorToCache())
                .toList();
    }

    private Function<Jugador, Jugador> saveJugadorToCache(){
        return jugador -> {
            this.cacheById.put(jugador.getJugadorId(), jugador);
            this.cacheByNombre.put(jugador.getNombre(), jugador);

            return jugador;
        };
    }
}
