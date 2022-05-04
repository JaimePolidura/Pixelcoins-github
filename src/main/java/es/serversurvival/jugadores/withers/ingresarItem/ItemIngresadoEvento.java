package es.serversurvival.jugadores.withers.ingresarItem;

import es.serversurvival.transacciones._shared.domain.TipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.jugadores._shared.newformat.domain.Jugador;
import es.serversurvival.transacciones._shared.domain.Transaccion;
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
