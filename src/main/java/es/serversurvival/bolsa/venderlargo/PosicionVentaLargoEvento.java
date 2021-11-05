package es.serversurvival.bolsa.venderlargo;

import es.serversurvival.bolsa._shared.posicionescerradas.mysql.PosicionCerrada;
import es.serversurvival.bolsa._shared.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival.bolsa._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival._shared.eventospixelcoins.PosicionCerradaEvento;
import es.serversurvival.transacciones.mySQL.TipoTransaccion;
import es.serversurvival.transacciones.mySQL.Transaccion;
import lombok.Getter;

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
        return new PosicionCerrada(-1, vendedor, tipoActivo, nombreValor, cantidad, precioApertura, fechaApertura, precioCierre, formatFecha(), rentabilidad, ticker, TipoPosicion.LARGO);
    }
}
