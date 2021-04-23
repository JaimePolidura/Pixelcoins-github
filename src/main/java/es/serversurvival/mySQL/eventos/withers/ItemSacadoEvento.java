package es.serversurvival.mySQL.eventos.withers;

import es.serversurvival.mySQL.enums.TipoTransaccion;
import es.serversurvival.mySQL.eventos.EventoTipoTransaccion;
import es.serversurvival.mySQL.eventos.PixelcoinsEvento;
import es.serversurvival.mySQL.tablasObjetos.Jugador;
import es.serversurvival.mySQL.tablasObjetos.Transaccion;
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
