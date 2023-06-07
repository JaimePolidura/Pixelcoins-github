package es.serversurvival.pixelcoins.bolsa._shared.posiciones;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.UUID;

@AllArgsConstructor
public final class Posicion {
    @Getter private final UUID posicionId;
    @Getter private final UUID activoBolsaId;
    @Getter private final UUID jugadorId;
    @Getter private final int cantidad;
    @Getter private final TipoBolsaApuesta tipoApuesta;
    @Getter private final TipoPosicion tipoPosicion;
    @Getter private final double precioApertura;
    @Getter private final LocalDateTime fechaApertura;
    @Getter private final double precioCierre;
    @Getter private final LocalDateTime fechaCierre;
    @Getter private final double rentabilidad;

    public static Comparator<Posicion> sortByRentabilidad() {
        return Comparator.comparing(Posicion::getRentabilidad)
                .reversed();
    }

    public static Comparator<Posicion> sortByFechaCierre() {
        return Comparator.comparing(Posicion::getFechaCierre)
                .reversed();
    }

    public boolean estaLaPosicionVacia() {
        return cantidad <= 0;
    }

    public Posicion cerrar(int cantidadACerrar, double precioCierre, double rentabilidad) {
        return new Posicion(posicionId, activoBolsaId, jugadorId, cantidadACerrar, tipoApuesta, TipoPosicion.CERRADO,
                precioApertura, fechaApertura, precioCierre, LocalDateTime.now(), rentabilidad);
    }

    public Posicion decrementarCantidad(int cantidadDecrementar) {
        return new Posicion(posicionId, activoBolsaId, jugadorId, cantidad - cantidadDecrementar, tipoApuesta,
                tipoPosicion, precioApertura, fechaApertura, precioCierre, fechaCierre, rentabilidad);
    }
}
