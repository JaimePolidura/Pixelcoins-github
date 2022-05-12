package es.serversurvival.bolsa.posicionesabiertas.comprarcorto;

import es.serversurvival.bolsa.other._shared.posicionescerradas.mysql.PosicionCerrada;
import es.serversurvival.bolsa.other._shared.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival.bolsa.other._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival._shared.eventospixelcoins.PosicionCerradaEvento;
import es.serversurvival.transacciones._shared.domain.Transaccion;

import java.util.UUID;

import static es.serversurvival.transacciones._shared.domain.TipoTransaccion.*;

public final class PosicionCompraCortoEvento extends PosicionCerradaEvento {
    protected final double revalorizacionTotal;

    public PosicionCompraCortoEvento(String vendedor, String ticker, String nombreValor, double precioApertura, String fechaApertura,
                                     double precioCierre, int cantidad, double rentabilidad, TipoActivo tipoActivo) {

        super(vendedor, ticker, nombreValor, precioApertura, fechaApertura, precioCierre, cantidad, rentabilidad, tipoActivo);

        this.revalorizacionTotal = (precioApertura - precioCierre) * cantidad;
    }

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), ticker, vendedor, (int) revalorizacionTotal, "", BOLSA_CORTO_COMPRA);
    }

    @Override
    public PosicionCerrada buildPosicionCerrada() {
        return new PosicionCerrada(-1, vendedor, tipoActivo, nombreValor, cantidad, precioApertura, fechaApertura, precioCierre, formatFecha(), rentabilidad, ticker, TipoPosicion.CORTO);
    }
}
