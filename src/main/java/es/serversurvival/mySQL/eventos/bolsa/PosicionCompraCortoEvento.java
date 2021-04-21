package es.serversurvival.mySQL.eventos.bolsa;

import es.serversurvival.mySQL.enums.TipoActivo;
import es.serversurvival.mySQL.tablasObjetos.PosicionCerrada;
import es.serversurvival.mySQL.tablasObjetos.Transaccion;

import static es.serversurvival.mySQL.enums.TipoPosicion.CORTO;
import static es.serversurvival.mySQL.enums.TipoTransaccion.*;

public final class PosicionCompraCortoEvento extends PosicionCerradaEvento{
    protected final double revalorizacionTotal;

    public PosicionCompraCortoEvento(String vendedor, String ticker, String nombreValor, double precioApertura, String fechaApertura,
                                     double precioCierre, int cantidad, double rentabilidad, TipoActivo tipoActivo) {

        super(vendedor, ticker, nombreValor, precioApertura, fechaApertura, precioCierre, cantidad, rentabilidad, tipoActivo);

        this.revalorizacionTotal = (precioApertura - precioCierre) * cantidad;
    }

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), ticker, vendedor, (int) revalorizacionTotal, "", BOLSA_CORTO_COMPRA);
    }

    @Override
    public PosicionCerrada buildPosicionCerrada() {
        return new PosicionCerrada(-1, vendedor, tipoActivo, nombreValor, cantidad, precioApertura, fechaApertura, precioCierre, formatFecha(), rentabilidad, ticker, CORTO);
    }
}
