package es.serversurvival.legacy.mySQL.eventos;

import es.serversurvival.nfs.transacciones.mySQL.Transaccion;

public interface EventoTipoTransaccion {
    Transaccion buildTransaccion();
}
