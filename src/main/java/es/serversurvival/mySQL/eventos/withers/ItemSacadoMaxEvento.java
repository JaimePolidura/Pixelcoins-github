package es.serversurvival.mySQL.eventos.withers;

import es.serversurvival.mySQL.eventos.TransactionEvent;
import es.serversurvival.mySQL.tablasObjetos.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static es.serversurvival.mySQL.enums.TipoTransaccion.*;

@AllArgsConstructor
public final class ItemSacadoMaxEvento extends TransactionEvent {
    @Getter private final String jugador;
    @Getter private final String itemNombre;
    @Getter private final int pixelcoins;

    @Override
    public Transaccion buildTransaccion() {
        return new Transaccion(-1, formatFecha(), jugador, "", pixelcoins, itemNombre, WITHERS_SACARMAX);
    }
}
