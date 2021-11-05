package es.serversurvival.bolsa.comprarlargo;

import es.serversurvival.bolsa._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival.shared.eventospixelcoins.PosicionAbiertaEvento;
import es.serversurvival.transacciones.mySQL.Transaccion;
import lombok.Getter;

import static es.serversurvival.transacciones.mySQL.TipoTransaccion.*;


public final class PosicionCompraLargoEvento extends PosicionAbiertaEvento {
    @Getter private final String alias;

    public PosicionCompraLargoEvento(String comprador, double precioUnidad, int cantidad, double precioTotal, String ticker,
                                     TipoActivo tipoActivo, String nombreValor, String alias) {
        super(comprador, precioUnidad, cantidad, precioTotal, ticker, tipoActivo, nombreValor);

        this.alias = alias;
    }

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), comprador, ticker, (int) precioTotal, ticker, BOLSA_COMPRA);
    }
}
