package es.serversurvival.legacy.mySQL.eventos.bolsa;

import es.serversurvival.nfs.transacciones.mySQL.Transaccion;
import es.serversurvival.nfs.transacciones.mySQL.TipoTransaccion;
import es.serversurvival.nfs.shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival.nfs.shared.eventospixelcoins.PixelcoinsEvento;
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
