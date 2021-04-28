package es.serversurvival.jugadores.withers.ingresarItem;

import es.serversurvival.transacciones.mySQL.TipoTransaccion;
import es.serversurvival.shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival.shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.transacciones.mySQL.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class ItemIngresadoEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final Jugador jugador;
    @Getter private final double pixelcoins;
    @Getter private final String nombreitem;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), "", jugador.getNombre(), (int) pixelcoins, nombreitem, TipoTransaccion.WITHERS_INGRESAR);
    }
}
