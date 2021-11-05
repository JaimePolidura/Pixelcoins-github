package es.serversurvival.jugadores.venderjugador;

import es.serversurvival._shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.transacciones.mySQL.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static es.serversurvival.transacciones.mySQL.TipoTransaccion.*;

@AllArgsConstructor
public final class ItemVendidoJugadorEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String comprador;
    @Getter private final String vendedor;
    @Getter private final double pixelcoins;
    @Getter private final String itemType;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), comprador, vendedor, (int) pixelcoins, itemType, JUGADOR_VENDER);
    }
}
