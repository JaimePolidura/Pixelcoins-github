package es.serversurvival.bolsa.posicionesabiertas.comprarlargo;

import es.serversurvival.bolsa.other._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival._shared.eventospixelcoins.PosicionAbiertaEvento;
import es.serversurvival.transacciones._shared.domain.Transaccion;
import lombok.Getter;

import java.util.UUID;

import static es.serversurvival.transacciones._shared.domain.TipoTransaccion.*;


public final class PosicionCompraLargoEvento extends PosicionAbiertaEvento {
    @Getter private final String alias;

    public PosicionCompraLargoEvento(String comprador, double precioUnidad, int cantidad, double precioTotal, String ticker,
                                     TipoActivo tipoActivo, String nombreValor, String alias) {
        super(comprador, precioUnidad, cantidad, precioTotal, ticker, tipoActivo, nombreValor);

        this.alias = alias;
    }

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), comprador, ticker, (int) precioTotal, ticker, BOLSA_COMPRA);
    }
}
