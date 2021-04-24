package es.serversurvival.legacy.mySQL.eventos;

import es.serversurvival.legacy.mySQL.tablasObjetos.Transaccion;

public interface EventoTipoTransaccion {
    Transaccion buildTransaccion();
}
