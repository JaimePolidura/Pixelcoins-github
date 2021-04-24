package es.serversurvival.legacy.mySQL.eventos.bolsa;

import es.serversurvival.legacy.mySQL.tablasObjetos.Transaccion;
import es.serversurvival.legacy.mySQL.enums.TipoTransaccion;
import es.serversurvival.legacy.mySQL.eventos.EventoTipoTransaccion;
import es.serversurvival.legacy.mySQL.eventos.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class DividendoPagadoEvento extends PixelcoinsEvento implements EventoTipoTransaccion {
    @Getter private final String jugador;
    @Getter private final String ticker;
    @Getter private final double pixelcoins;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), ticker, jugador, (int) pixelcoins, "", TipoTransaccion.BOLSA_DIVIDENDO);
    }
}
