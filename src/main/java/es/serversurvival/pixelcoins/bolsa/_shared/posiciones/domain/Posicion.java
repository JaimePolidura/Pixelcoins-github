package es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public final class Posicion {
    @Getter private UUID posicionId;
    @Getter private UUID activoBolsaId;
    @Getter private UUID jugadorId;
    @Getter private int cantidad;
    @Getter private TipoBolsaApuesta tipoApuesta;
    @Getter private TipoPosicion tipoPosicion;
    @Getter private double precioApertura;
    @Getter private LocalDateTime fechaApertura;
    @Getter private double precioCierre;
    @Getter private LocalDateTime fechaCierre;
    @Getter private double rentabilidad;

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
        return new Posicion(UUID.randomUUID(), activoBolsaId, jugadorId, cantidadACerrar, tipoApuesta, TipoPosicion.CERRADO,
                precioApertura, fechaApertura, precioCierre, LocalDateTime.now(), rentabilidad);
    }

    public Posicion decrementarCantidad(int cantidadDecrementar) {
        return new Posicion(posicionId, activoBolsaId, jugadorId, cantidad - cantidadDecrementar, tipoApuesta,
                tipoPosicion, precioApertura, fechaApertura, precioCierre, fechaCierre, rentabilidad);
    }
}
