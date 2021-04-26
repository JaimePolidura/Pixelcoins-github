package es.serversurvival.legacy.mySQL.eventos.bolsa;

import es.serversurvival.nfs.bolsa.llamadasapi.TipoActivo;
import es.serversurvival.nfs.transacciones.mySQL.Transaccion;

import static es.serversurvival.legacy.mySQL.enums.TipoTransaccion.*;

public final class PosicionVentaCortoEvento extends PosicionAbiertaEvento{
    public PosicionVentaCortoEvento(String comprador, double precioUnidad, int cantidad, double precioTotal, String ticker,
                                    TipoActivo tipoActivo, String nombreValor) {
        super(comprador, precioUnidad, cantidad, precioTotal, ticker, tipoActivo, nombreValor);
    }

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), ticker, comprador, (int) precioTotal, ticker, BOLSA_CORTO_VENTA);
    }
}
