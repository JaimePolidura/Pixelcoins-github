package es.serversurvival.jugadores._shared.application;

import es.dependencyinjector.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.cache.UnlimitedCacheSize;
import es.serversurvival.jugadores._shared.domain.JugadoresRepository;
import es.serversurvival.jugadores._shared.domain.Jugador;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class JugadoresService {
    private JugadoresRepository repositoryDb;
    private UnlimitedCacheSize<String, Jugador> cache;

    public JugadoresService () {}

    public JugadoresService(JugadoresRepository jugadoresRepository) {
        this.repositoryDb = jugadoresRepository;
        this.cache = new UnlimitedCacheSize<>();
    }

    public void save(Jugador jugador){
        this.repositoryDb.save(jugador);
        this.cache.put(jugador.getNombre(), jugador);
    }

    public void save(UUID jugadorId, String nombre) {
        var jugador = new Jugador(jugadorId, nombre, 0, 0,
                0, 0, 0, 0, this.generearNumeroCuenta());

        this.repositoryDb.save(jugador);
        this.cache.put(jugador.getNombre(), jugador);
    }

    public int generearNumeroCuenta () {
        return (int) (Math.random() * 99999);
    }

    public Jugador getByNombre(String nombre){
        return this.cache.find(nombre).orElseGet(() -> this.repositoryDb.findByNombre(nombre)
                .map(saveJugadorToCache())
                .orElseThrow(() -> new ResourceNotFound("Jugador no encontrado")));
    }

    public Jugador getById(UUID jugadorId){
        var cachedJugador = this.cache.findValueIf(jugador -> jugador.getJugadorId().equals(jugadorId));

        return cachedJugador.orElseGet(() -> this.repositoryDb.findById(jugadorId)
                .map(saveJugadorToCache())
                .orElseThrow(() -> new ResourceNotFound("Jugador no encontrado")));
    }

    public Optional<Jugador> findByNombre(String nombre){
        return this.repositoryDb.findByNombre(nombre);
    }

    public Optional<Jugador> findById(UUID jugadorId){
        return this.repositoryDb.findById(jugadorId);
    }

    public List<Jugador> findAll(){
        return this.repositoryDb.findAll().stream()
                .map(saveJugadorToCache())
                .toList();
    }

    public List<Jugador> findBy(Predicate<? super Jugador> condition){
        return this.cache.all().stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    public List<Jugador> sortJugadoresBy(Comparator<? super Jugador> jugadorComparator){
        return this.findAll().stream()
                .sorted(jugadorComparator)
                .collect(Collectors.toList());
    }

    public void realizarTransferencia(Jugador pagador, Jugador pagado, double pixelcoins) {
        this.save(pagador.decrementPixelcoinsBy(pixelcoins)
                .incrementGastosBy(pixelcoins));

        this.save(pagado.incrementPixelcoinsBy(pixelcoins)
                .incrementNVentas()
                .incrementIngresosBy(pixelcoins));
    }

    public Map<String, Jugador> getMapAllJugadores () {
        Map<String, Jugador> jugadoresMap = new HashMap<>();
        findAll().forEach(jugador -> jugadoresMap.put(jugador.getNombre(), jugador));

        return jugadoresMap;
    }

    private Function<Jugador, Jugador> saveJugadorToCache(){
        return jugador -> {
            this.cache.put(jugador.getNombre(), jugador);

            return jugador;
        };
    }

    public void initalizeCache(){
        this.repositoryDb.findAll().forEach(jugador -> {
            this.cache.put(jugador.getNombre(), jugador);
        });
    }
}
