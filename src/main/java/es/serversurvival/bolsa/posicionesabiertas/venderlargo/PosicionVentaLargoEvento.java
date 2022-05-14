package es.serversurvival.bolsa.posicionesabiertas.venderlargo;

import es.serversurvival.bolsa.posicionescerradas._shared.domain.PosicionCerrada;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion;
import es.serversurvival.bolsa.other._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival._shared.eventospixelcoins.PosicionCerradaEvento;
import es.serversurvival.transacciones._shared.domain.TipoTransaccion;
import es.serversurvival.transacciones._shared.domain.Transaccion;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

import static es.serversurvival._shared.mysql.AllMySQLTablesInstances.dateFormater;

public final class PosicionVentaLargoEvento extends PosicionCerradaEvento {
    @Getter private final double valorTotal;
    @Getter private final double resultado;

    public PosicionVentaLargoEvento(String vendedor, String ticker, String nombreValor, double precioApertura,
                                    String fechaApertura, double precioCierre, int cantidad, TipoActivo tipoActivo) {
        super(vendedor, ticker, nombreValor, precioApertura, fechaApertura, precioCierre, cantidad, tipoActivo);

        this.valorTotal = precioCierre * cantidad;
        this.resultado = (precioCierre - precioApertura) * cantidad;
    }

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), ticker, vendedor, (int) valorTotal, "", TipoTransaccion.BOLSA_VENTA);
    }

    @Override
    public PosicionCerrada buildPosicionCerrada() {
        String fechaCierre = dateFormater.format(new Date());

        return new PosicionCerrada(UUID.randomUUID(), vendedor, tipoActivo, nombreAcitvo, cantidad, precioApertura, fechaApertura,
                precioCierre, fechaCierre, TipoPosicion.LARGO);
    }
}
