package es.serversurvival.mySQL.eventos;

import es.serversurvival.mySQL.tablasObjetos.Transaccion;

public final class TransferenciaEvent extends TransactionEvent{
    @Override
    public Transaccion buildTransaccion() {
        return null;
    }
}
