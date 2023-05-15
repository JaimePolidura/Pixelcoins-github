package es.serversurvival.v1.bolsa.posicionesabiertas._shared.domain;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.v1.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import es.serversurvival.v1.bolsa.posicionescerradas._shared.domain.TipoPosicion;
import es.serversurvival.v1.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival.v1.transacciones._shared.domain.Transaccion;
import es.serversurvival.v1.transacciones._shared.domain.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

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
                (int) precioTotal, nombreActivo, tipoPosicion == TipoPosicion.LARGO ? TipoTransaccion.BOLSA_LARGO_COMPRA : TipoTransaccion.BOLSA_CORTO_VENTA);
    }

    public static PosicionAbiertaEvento of(String comprador, String ticker, int cantidadPosicion, double precioUnidad,
                                           TipoActivo tipoActivo, double precioTotal, String nombreActivo, TipoPosicion tipoPosicion){
        return new PosicionAbiertaEvento(comprador, ticker, cantidadPosicion, precioUnidad, tipoActivo, precioTotal, nombreActivo, tipoPosicion);
    }
}
