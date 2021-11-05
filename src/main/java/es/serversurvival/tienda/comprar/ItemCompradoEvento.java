package es.serversurvival.tienda.comprar;

import es.serversurvival._shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.transacciones.mySQL.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static es.serversurvival.transacciones.mySQL.TipoTransaccion.*;

@AllArgsConstructor
public final class ItemCompradoEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String vendedor;
    @Getter private final String comprador;
    @Getter private final String objeto;
    @Getter private final int cantidadComprada;
    @Getter private final double precioUnidad;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), comprador, vendedor, cantidadComprada, objeto, TIENDA_VENTA);
    }
}
