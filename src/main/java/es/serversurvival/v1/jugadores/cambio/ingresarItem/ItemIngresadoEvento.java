package es.serversurvival.v1.jugadores.cambio.ingresarItem;

import es.serversurvival.v1.transacciones._shared.domain.TipoTransaccion;
import es.serversurvival.v1.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.v1.jugadores._shared.domain.Jugador;
import es.serversurvival.v1.transacciones._shared.domain.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class ItemIngresadoEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final Jugador jugador;
    @Getter private final double pixelcoins;
    @Getter private final String nombreitem;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(UUID.randomUUID(), formatFecha(), "", jugador.getNombre(), (int) pixelcoins, nombreitem, TipoTransaccion.WITHERS_INGRESAR);
    }
}
