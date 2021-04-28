package es.serversurvival.shared.eventospixelcoins;

import es.serversurvival.transacciones.mySQL.Transaccion;

public interface EventoTipoTransaccion {
    Transaccion buildTransaccion();
}
