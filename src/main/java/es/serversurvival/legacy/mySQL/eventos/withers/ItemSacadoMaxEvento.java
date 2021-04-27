package es.serversurvival.legacy.mySQL.eventos.withers;

import es.serversurvival.nfs.shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival.nfs.shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.nfs.jugadores.mySQL.Jugador;
import es.serversurvival.nfs.transacciones.mySQL.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static es.serversurvival.nfs.transacciones.mySQL.TipoTransaccion.*;

@AllArgsConstructor
public final class ItemSacadoMaxEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final Jugador jugador;
    @Getter private final String itemNombre;
    @Getter private final int pixelcoins;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), jugador.getNombre(), "", pixelcoins, itemNombre, WITHERS_SACARMAX);
    }
}
