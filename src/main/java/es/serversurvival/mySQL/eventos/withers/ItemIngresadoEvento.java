package es.serversurvival.mySQL.eventos.withers;

import es.serversurvival.mySQL.enums.TipoTransaccion;
import es.serversurvival.mySQL.eventos.TransactionEvent;
import es.serversurvival.mySQL.tablasObjetos.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class ItemIngresadoEvento extends TransactionEvent {
    @Getter private final String jugador;
    @Getter private final double pixelcoins;
    @Getter private final String nombreitem;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), "", jugador, (int) pixelcoins, nombreitem, TipoTransaccion.WITHERS_INGRESAR);
    }
}
