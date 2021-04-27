package es.serversurvival.legacy.mySQL.eventos.bolsa;

import es.serversurvival.nfs.bolsa.llamadasapi.mysql.TipoActivo;
import es.serversurvival.nfs.shared.eventospixelcoins.PosicionCerradaEvento;
import es.serversurvival.nfs.transacciones.mySQL.TipoTransaccion;
import es.serversurvival.nfs.bolsa.posicionescerradas.mysql.PosicionCerrada;
import es.serversurvival.nfs.transacciones.mySQL.Transaccion;
import lombok.Getter;

import static es.serversurvival.nfs.bolsa.posicionescerradas.mysql.TipoPosicion.*;

public final class PosicionVentaLargoEvento extends PosicionCerradaEvento {
    @Getter private final double valorTotal;
    @Getter private final double resultado;

    public PosicionVentaLargoEvento(String vendedor, String ticker, String nombreValor, double precioApertura,
                                    String fechaApertura, double precioCierre, int cantidad, double rentabilidad, TipoActivo tipoActivo) {
        super(vendedor, ticker, nombreValor, precioApertura, fechaApertura, precioCierre, cantidad, rentabilidad, tipoActivo);

        this.valorTotal = precioCierre * cantidad;
        this.resultado = (precioCierre - precioApertura) * cantidad;
    }

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), ticker, vendedor, (int) valorTotal, "", TipoTransaccion.BOLSA_VENTA);
    }

    @Override
    public PosicionCerrada buildPosicionCerrada() {
        return new PosicionCerrada(-1, vendedor, tipoActivo, nombreValor, cantidad, precioApertura, fechaApertura, precioCierre, formatFecha(), rentabilidad, ticker, LARGO);
    }
}
