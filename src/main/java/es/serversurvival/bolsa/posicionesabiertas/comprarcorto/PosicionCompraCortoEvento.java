package es.serversurvival.bolsa.posicionesabiertas.comprarcorto;

import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.PosicionCerrada;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.PosicionCerradaEvento;
import es.serversurvival.transacciones._shared.domain.Transaccion;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static es.serversurvival.transacciones._shared.domain.TipoTransaccion.*;

public final class PosicionCompraCortoEvento extends PosicionCerradaEvento {
    protected final double revalorizacionTotal;

    public PosicionCompraCortoEvento(String vendedor, String ticker, String nombreValor, double precioApertura, String fechaApertura,
                                     double precioCierre, int cantidad, TipoActivo tipoActivo) {

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

    public static PosicionCompraCortoEvento of(String jugaodor, String nombreActivo, String nombreActivoLargo, double precioApertura,
                                               String fechaApertura, double precioCierre, int cantidad, TipoActivo tipoActivo){
        return new PosicionCompraCortoEvento(jugaodor, nombreActivo, nombreActivoLargo, precioApertura, fechaApertura, precioCierre, cantidad, tipoActivo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PosicionCompraCortoEvento that = (PosicionCompraCortoEvento) o;
        return Double.compare(that.revalorizacionTotal, revalorizacionTotal) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(revalorizacionTotal, super.hashCode());
    }
}
