package es.serversurvival.legacy.mySQL.eventos.bolsa;

import es.serversurvival.nfs.bolsa.llamadasapi.mysql.TipoActivo;
import es.serversurvival.nfs.bolsa.posicionescerradas.mysql.PosicionCerrada;
import es.serversurvival.nfs.shared.eventospixelcoins.PosicionCerradaEvento;
import es.serversurvival.nfs.transacciones.mySQL.Transaccion;

import static es.serversurvival.nfs.bolsa.posicionescerradas.mysql.TipoPosicion.CORTO;
import static es.serversurvival.nfs.transacciones.mySQL.TipoTransaccion.*;

public final class PosicionCompraCortoEvento extends PosicionCerradaEvento {
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
