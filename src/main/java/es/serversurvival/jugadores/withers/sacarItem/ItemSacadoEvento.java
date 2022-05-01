package es.serversurvival.jugadores.withers.sacarItem;

import es.serversurvival.transacciones.mySQL.TipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.jugadores._shared.newformat.domain.Jugador;
import es.serversurvival.transacciones.mySQL.Transaccion;
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
