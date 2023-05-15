package es.serversurvival.v1.tienda.comprar;

import es.serversurvival.v1.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.v1.transacciones._shared.domain.Transaccion;
import es.serversurvival.v1.transacciones._shared.domain.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class ObjetoTiendaComprado extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String vendedor;
    @Getter private final String comprador;
    @Getter private final String objeto;
    @Getter private final int cantidadComprada;
    @Getter private final double precioUnidad;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), comprador, vendedor, cantidadComprada, objeto, TipoTransaccion.TIENDA_VENTA);
    }
}
