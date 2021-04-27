package es.serversurvival.nfs.shared.eventospixelcoins;

import es.serversurvival.nfs.transacciones.mySQL.Transaccion;

public interface EventoTipoTransaccion {
    Transaccion buildTransaccion();
}
