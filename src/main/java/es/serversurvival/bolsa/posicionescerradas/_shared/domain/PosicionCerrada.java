package es.serversurvival.bolsa.posicionescerradas._shared.domain;

import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class PosicionCerrada {
    @Getter private final UUID posicionCerradaId;
    @Getter private final String jugador;
    @Getter private final TipoActivo tipoActivo;
    @Getter private final String nombreActivo;
    @Getter private final int cantidad;
    @Getter private final double precioApertura;
    @Getter private final String fechaApertura;
    @Getter private final double precioCierre;
    @Getter private final String fechaCierre;
    @Getter private final TipoPosicion tipoPosicion;

    public PosicionCerrada withJugador(String jugador){
        return new PosicionCerrada(posicionCerradaId, jugador, tipoActivo, nombreActivo, cantidad, precioApertura,
                fechaApertura, precioCierre, fechaCierre, tipoPosicion);
    }

    public double calculateRentabildiad(){
        return  this.tipoPosicion == TipoPosicion.LARGO ?
                ((this.precioCierre / this.precioApertura) - 1) * 100 :
                ((this.precioApertura / this.precioCierre) - 1) * 100;
    }

    public boolean esSimilar (PosicionCerrada posicionAComparar) {
        return posicionAComparar.calculateRentabildiad() == calculateRentabildiad() &&
                posicionAComparar.getNombreActivo().equalsIgnoreCase(nombreActivo) &&
                jugador.equalsIgnoreCase(posicionAComparar.getJugador());
    }
}
