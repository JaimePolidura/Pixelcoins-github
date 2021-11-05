package es.serversurvival.bolsa.vendercorto;

import es.serversurvival.bolsa._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival._shared.eventospixelcoins.PosicionAbiertaEvento;
import es.serversurvival.transacciones.mySQL.Transaccion;

import static es.serversurvival.transacciones.mySQL.TipoTransaccion.*;

public final class PosicionVentaCortoEvento extends PosicionAbiertaEvento {
    public PosicionVentaCortoEvento(String comprador, double precioUnidad, int cantidad, double precioTotal, String ticker,
                                    TipoActivo tipoActivo, String nombreValor) {
        super(comprador, precioUnidad, cantidad, precioTotal, ticker, tipoActivo, nombreValor);
    }

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), ticker, comprador, (int) precioTotal, ticker, BOLSA_CORTO_VENTA);
    }
}
