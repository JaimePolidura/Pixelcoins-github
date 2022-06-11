package es.serversurvival.bolsa.posicionesabiertas._shared.domain;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion;
import es.serversurvival.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival.transacciones._shared.domain.Transaccion;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

import static es.serversurvival.transacciones._shared.domain.TipoTransaccion.BOLSA_LARGO_COMPRA;
import static es.serversurvival.transacciones._shared.domain.TipoTransaccion.BOLSA_CORTO_VENTA;

@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public final class PosicionAbiertaEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String comprador;
    @Getter private final String nombreActivo;
    @Getter private final int cantidadPosicion;
    @Getter private final double precioUnidad;
    @Getter private final TipoActivo tipoActivo;
    @Getter private final double precioTotal;
    @Getter private final String nombreActivoLargo;
    @Getter private final TipoPosicion tipoPosicion;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), nombreActivo, comprador,
                (int) precioTotal, nombreActivo, tipoPosicion == TipoPosicion.LARGO ? BOLSA_LARGO_COMPRA : BOLSA_CORTO_VENTA);
    }

    public static PosicionAbiertaEvento of(String comprador, String ticker, int cantidadPosicion, double precioUnidad,
                                           TipoActivo tipoActivo, double precioTotal, String nombreActivo, TipoPosicion tipoPosicion){
        return new PosicionAbiertaEvento(comprador, ticker, cantidadPosicion, precioUnidad, tipoActivo, precioTotal, nombreActivo, tipoPosicion);
    }
}
