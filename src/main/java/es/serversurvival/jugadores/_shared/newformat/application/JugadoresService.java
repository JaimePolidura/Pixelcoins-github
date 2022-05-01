package es.serversurvival.jugadores._shared.newformat.application;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.jugadores._shared.newformat.domain.Jugador;
import es.serversurvival.jugadores._shared.newformat.domain.JugadoresRepository;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class JugadoresService {
    private final JugadoresRepository jugadoresRepository;

    public JugadoresService(JugadoresRepository jugadoresRepository) {
        this.jugadoresRepository = jugadoresRepository;
    }

    public void save(Jugador jugador){
        this.jugadoresRepository.save(jugador);
    }

    public void save(UUID jugadorId, String nombre){
        this.jugadoresRepository.save(new Jugador(jugadorId, nombre, 0, 0,
                0, 0, 0, 0, this.generearNumeroCuenta()));
    }

    private int generearNumeroCuenta () {
        return (int) (Math.random() * 99999);
    }

    public boolean estaRegistradoNumeroCuentaPara(String nombre, int numeroCuenta){
        Optional<Jugador> jugador = this.jugadoresRepository.findByNombre(nombre);

        return jugador.isPresent() && jugador.get().getNumeroVerificacionCuenta() == numeroCuenta;
    }

    public Jugador getJugadorByNombre(String nombre){
        return this.jugadoresRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResourceNotFound("Jugador no encontrado"));
    }

    public Jugador getJugadorById(UUID jugadorId){
        return this.jugadoresRepository.findByJugadorId(jugadorId)
                .orElseThrow(() -> new ResourceNotFound("Jugador no encontrado"));
    }

    public List<Jugador> findAll(){
        return this.jugadoresRepository.findAll();
    }

    public List<Jugador> findBy(Predicate<? super Jugador> condition){
        return this.findAll().stream()
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

        this.jugadoresRepository.save(pagadorChangedPixelcoins);
        this.jugadoresRepository.save(pagadoChangedPixelcoins);
    }

    public void realizarTransferenciaConEstadisticas (String nombrePagador, String nombrePagado, double pixelcoins) {
        Jugador pagador =  this.getJugadorByNombre(nombrePagador)
                .decrementPixelcoinsBy(pixelcoins)
                .incrementGastosBy(pixelcoins);

        Jugador pagado = this.getJugadorByNombre(nombrePagado)
                .incrementPixelcoinsBy(pixelcoins)
                .incrementNVentas()
                .incrementIngresosBy(pixelcoins);

        this.jugadoresRepository.save(pagador);
        this.jugadoresRepository.save(pagado);
    }
}
