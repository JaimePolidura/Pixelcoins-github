package es.serversurvival.mySQL.eventos.withers;

import es.serversurvival.mySQL.enums.TipoTransaccion;
import es.serversurvival.mySQL.eventos.EventoTipoTransaccion;
import es.serversurvival.mySQL.eventos.PixelcoinsEvento;
import es.serversurvival.mySQL.tablasObjetos.Jugador;
import es.serversurvival.mySQL.tablasObjetos.Transaccion;
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
