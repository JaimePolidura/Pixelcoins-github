package es.serversurvival.mySQL.eventos;

import es.jaime.Event;
import es.serversurvival.mySQL.tablasObjetos.Transaccion;
import es.serversurvival.util.Funciones;

public abstract class TransactionEvent extends Event {
    public abstract Transaccion buildTransaccion();

    protected String formatFecha () {
        return getTimeOnCreated().format(Funciones.DATE_FORMATER);
    }
}
