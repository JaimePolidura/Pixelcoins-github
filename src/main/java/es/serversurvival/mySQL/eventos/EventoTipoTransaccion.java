package es.serversurvival.mySQL.eventos;

import es.serversurvival.mySQL.tablasObjetos.Transaccion;

public interface EventoTipoTransaccion {
    Transaccion buildTransaccion();
}
