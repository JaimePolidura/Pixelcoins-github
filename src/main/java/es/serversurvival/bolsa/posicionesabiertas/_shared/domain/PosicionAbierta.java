package es.serversurvival.bolsa.posicionesabiertas._shared.domain;

import es.jaime.javaddd.domain.Aggregate;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;


@AllArgsConstructor
public final class PosicionAbierta extends Aggregate {
    @Getter private final UUID posicionAbiertaId;
    @Getter private final String jugador;
    @Getter private final SupportedTipoActivo tipoActivo;
    @Getter private final String nombreActivo;
    @Getter private final int cantidad;
    @Getter private final double precioApertura;
    @Getter private final String fechaApertura;
    @Getter private final TipoPosicion tipoPosicion;

    public PosicionAbierta withCantidad(int cantidad){
        return new PosicionAbierta(posicionAbiertaId, jugador, tipoActivo, nombreActivo,
                cantidad, precioApertura, fechaApertura, tipoPosicion);
    }

    public PosicionAbierta withPrecioApertura(double newPrecioApertura){
        return new PosicionAbierta(posicionAbiertaId, jugador, tipoActivo, nombreActivo,
                cantidad, newPrecioApertura, fechaApertura, tipoPosicion);
    }

    public PosicionAbierta withJugador(String jugador){
        return new PosicionAbierta(posicionAbiertaId, jugador, tipoActivo, nombreActivo,
                cantidad, precioApertura, fechaApertura, tipoPosicion);
    }

    public boolean esLargo () {
        return tipoPosicion == TipoPosicion.LARGO;
    }

    public boolean esCorto () {
        return tipoPosicion == TipoPosicion.CORTO;
    }

    public boolean esTipoAccion () {
        return tipoActivo == SupportedTipoActivo.ACCIONES;
    }
}
