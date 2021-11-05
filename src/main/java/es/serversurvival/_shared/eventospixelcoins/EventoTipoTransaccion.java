package es.serversurvival._shared.eventospixelcoins;

import es.serversurvival.transacciones.mySQL.Transaccion;

public interface EventoTipoTransaccion {
    Transaccion buildTransaccion();
}
