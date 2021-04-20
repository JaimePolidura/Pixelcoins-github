package es.serversurvival.mySQL.eventos.bolsa;

import es.serversurvival.mySQL.enums.TipoActivo;
import es.serversurvival.mySQL.tablasObjetos.Transaccion;

import static es.serversurvival.mySQL.enums.TipoTransaccion.*;

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
