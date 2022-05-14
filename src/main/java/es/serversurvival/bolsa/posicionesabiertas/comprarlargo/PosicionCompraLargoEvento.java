package es.serversurvival.bolsa.posicionesabiertas.comprarlargo;

import es.serversurvival._shared.eventospixelcoins.PosicionAbiertaEvento;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import es.serversurvival.transacciones._shared.domain.Transaccion;

import java.util.UUID;

import static es.serversurvival.transacciones._shared.domain.TipoTransaccion.*;


public final class PosicionCompraLargoEvento extends PosicionAbiertaEvento {
    public PosicionCompraLargoEvento(String comprador, double precioUnidad, int cantidad, double precioTotal, String ticker,
                                     SupportedTipoActivo tipoActivo) {
        super(comprador, precioUnidad, cantidad, precioTotal, ticker, tipoActivo, null);
    }

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), comprador, ticker, (int) precioTotal, ticker, BOLSA_COMPRA);
    }
}
