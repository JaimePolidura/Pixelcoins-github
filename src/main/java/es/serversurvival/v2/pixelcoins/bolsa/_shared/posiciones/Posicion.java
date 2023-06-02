package es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
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

    public boolean estaLaPosicionVacia() {
        return cantidad <= 0;
    }

    public Posicion cerrar(int cantidadACerrar, double precioCierre) {
        return new Posicion(posicionId, activoBolsaId, jugadorId, cantidadACerrar, tipoApuesta, TipoPosicion.CERRADO,
                precioApertura, fechaApertura, precioCierre, LocalDateTime.now());
    }

    public Posicion decrementarCantidad(int cantidadDecrementar) {
        return new Posicion(posicionId, activoBolsaId, jugadorId, cantidad - cantidadDecrementar, tipoApuesta,
                tipoPosicion, precioApertura, fechaApertura, precioCierre, fechaCierre);
    }
}
