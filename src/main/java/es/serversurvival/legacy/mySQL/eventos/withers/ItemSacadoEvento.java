package es.serversurvival.legacy.mySQL.eventos.withers;

import es.serversurvival.legacy.mySQL.enums.TipoTransaccion;
import es.serversurvival.legacy.mySQL.eventos.EventoTipoTransaccion;
import es.serversurvival.legacy.mySQL.eventos.PixelcoinsEvento;
import es.serversurvival.nfs.jugadores.mySQL.Jugador;
import es.serversurvival.nfs.transacciones.mySQL.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class ItemSacadoEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final Jugador jugador;
    @Getter private final String itemNombre;
    @Getter private final int pixelcoins;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), jugador.getNombre(), "", pixelcoins, itemNombre, TipoTransaccion.WITHERS_SACAR);
    }
}
