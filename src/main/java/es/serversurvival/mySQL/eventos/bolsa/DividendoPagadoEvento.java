package es.serversurvival.mySQL.eventos.bolsa;

import es.serversurvival.mySQL.enums.TipoTransaccion;
import es.serversurvival.mySQL.eventos.TransactionEvent;
import es.serversurvival.mySQL.tablasObjetos.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class DividendoPagadoEvento extends TransactionEvent {
    @Getter private final String jugador;
    @Getter private final String ticker;
    @Getter private final double pixelcoins;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), ticker, jugador, (int) pixelcoins, "", TipoTransaccion.BOLSA_DIVIDENDO);
    }
}
