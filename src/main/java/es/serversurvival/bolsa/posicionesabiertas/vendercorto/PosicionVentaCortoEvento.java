package es.serversurvival.bolsa.posicionesabiertas.vendercorto;

import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbiertaEvento;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import es.serversurvival.transacciones._shared.domain.Transaccion;

import java.util.UUID;

import static es.serversurvival.transacciones._shared.domain.TipoTransaccion.*;

public final class PosicionVentaCortoEvento extends PosicionAbiertaEvento {
    public PosicionVentaCortoEvento(String comprador, double precioUnidad, int cantidad, double precioTotal, String ticker,
                                    SupportedTipoActivo tipoActivo, String nombreValor) {
        super(comprador, precioUnidad, cantidad, precioTotal, ticker, tipoActivo, nombreValor);
    }

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), ticker, comprador, (int) precioTotal, ticker, BOLSA_CORTO_VENTA);
    }
}
