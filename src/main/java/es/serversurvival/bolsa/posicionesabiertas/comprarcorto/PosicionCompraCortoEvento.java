package es.serversurvival.bolsa.posicionesabiertas.comprarcorto;

import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.PosicionCerrada;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion;
import es.serversurvival._shared.eventospixelcoins.PosicionCerradaEvento;
import es.serversurvival.transacciones._shared.domain.Transaccion;

import java.util.Date;
import java.util.UUID;

import static es.serversurvival.transacciones._shared.domain.TipoTransaccion.*;

public final class PosicionCompraCortoEvento extends PosicionCerradaEvento {
    protected final double revalorizacionTotal;

    public PosicionCompraCortoEvento(String vendedor, String ticker, String nombreValor, double precioApertura, String fechaApertura,
                                     double precioCierre, int cantidad, double rentabilidad, SupportedTipoActivo tipoActivo) {

        super(vendedor, ticker, nombreValor, precioApertura, fechaApertura, precioCierre, cantidad, tipoActivo);

        this.revalorizacionTotal = (precioApertura - precioCierre) * cantidad;
    }

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), ticker, vendedor, (int) revalorizacionTotal, "", BOLSA_CORTO_COMPRA);
    }

    @Override
    public PosicionCerrada buildPosicionCerrada() {
        String fechaCierre = Funciones.DATE_FORMATER_LEGACY.format(new Date());


        return new PosicionCerrada(UUID.randomUUID(), vendedor, tipoActivo, nombreAcitvo, cantidad, precioApertura,
                fechaApertura, precioCierre, fechaCierre, TipoPosicion.CORTO);
    }
}
