package es.serversurvival._shared.eventospixelcoins;

import es.serversurvival.transacciones._shared.domain.Transaccion;

public interface EventoTipoTransaccion {
    Transaccion buildTransaccion();
}
