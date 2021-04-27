package es.serversurvival.legacy.mySQL.eventos.withers;

import es.serversurvival.nfs.transacciones.mySQL.TipoTransaccion;
import es.serversurvival.nfs.shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival.nfs.shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.nfs.jugadores.mySQL.Jugador;
import es.serversurvival.nfs.transacciones.mySQL.Transaccion;
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
