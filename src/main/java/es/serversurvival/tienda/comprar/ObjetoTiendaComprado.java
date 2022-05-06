package es.serversurvival.tienda.comprar;

import es.serversurvival._shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.transacciones._shared.domain.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

import static es.serversurvival.transacciones._shared.domain.TipoTransaccion.*;

@AllArgsConstructor
public final class ObjetoTiendaComprado extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String vendedor;
    @Getter private final String comprador;
    @Getter private final String objeto;
    @Getter private final int cantidadComprada;
    @Getter private final double precioUnidad;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), comprador, vendedor, cantidadComprada, objeto, TIENDA_VENTA);
    }
}
