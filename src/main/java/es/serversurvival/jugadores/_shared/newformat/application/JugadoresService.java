package es.serversurvival.jugadores._shared.newformat.application;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.cache.Cache;
import es.serversurvival._shared.cache.LRUCache;
import es.serversurvival.jugadores._shared.newformat.domain.Jugador;
import es.serversurvival.jugadores._shared.newformat.domain.JugadoresRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class JugadoresService {
    private final JugadoresRepository repositoryDb;
    private final Cache<String, Jugador> cache;

    public JugadoresService() {
        this.repositoryDb = DependecyContainer.get(JugadoresRepository.class);
        this.cache = new LRUCache<>(150);
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

    public Jugador getJugadorByNombre(String nombre){
        var cachedJugador = this.cache.find(nombre);

        return cachedJugador.orElseGet(() -> this.repositoryDb.findByNombre(nombre)
                .map(saveJugadorToCache())
                .orElseThrow(() -> new ResourceNotFound("Jugador no encontrado")));
    }

    public Jugador getJugadorById(UUID jugadorId){
        var cachedJugador = this.cache.findValue(jugador -> jugador.getJugadorId().equals(jugadorId));

        return cachedJugador.orElseGet(() -> this.repositoryDb.findById(jugadorId)
                .map(saveJugadorToCache())
                .orElseThrow(() -> new ResourceNotFound("Jugador no encontrado")));
    }

    public boolean estaRegistradoNumeroCuentaPara(String nombre, int numeroVerificacionCuenta){
        return this.getJugadorByNombre(nombre).getNumeroVerificacionCuenta() == numeroVerificacionCuenta;
    }

    public List<Jugador> findAll(){
        return this.repositoryDb.findAll();
    }

    public List<Jugador> findBy(Predicate<? super Jugador> condition){
        List<Jugador> listToFind = this.cache.isFull() ? this.repositoryDb.findAll() : this.cache.all();

        return listToFind.stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    public List<Jugador> sortJugadoresBy(Comparator<? super Jugador> jugadorComparator){
        return this.findAll().stream()
                .sorted(jugadorComparator)
                .collect(Collectors.toList());
    }

    public void realizarTransferencia (String nombrePagador, String nombrePagado, double pixelcoins) {
        Jugador pagadorChangedPixelcoins = this.getJugadorByNombre(nombrePagador)
                .decrementPixelcoinsBy(pixelcoins);
        Jugador pagadoChangedPixelcoins = this.getJugadorByNombre(nombrePagado)
                .incrementPixelcoinsBy(pixelcoins);

        this.save(pagadorChangedPixelcoins);
        this.save(pagadoChangedPixelcoins);
    }

    public void realizarTransferenciaConEstadisticas (Jugador pagador, Jugador pagado, double pixelcoins) {
        this.save(pagador.decrementPixelcoinsBy(pixelcoins)
                .incrementGastosBy(pixelcoins));

        this.save(pagado.incrementPixelcoinsBy(pixelcoins)
                .incrementNVentas()
                .incrementIngresosBy(pixelcoins));
    }

    private Function<Jugador, Jugador> saveJugadorToCache(){
        return jugador -> {
            this.cache.put(jugador.getNombre(), jugador);

            return jugador;
        };
    }
}
