package es.serversurvival.transacciones.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival.shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.transacciones.mySQL.Transaccion;
import es.serversurvival.transacciones.mySQL.Transacciones;

public final class OnTransaccion {
    @EventListener({EventoTipoTransaccion.class})
    public void onTransaccion (PixelcoinsEvento evento) {
        Transaccion transaccion = ((EventoTipoTransaccion) evento).buildTransaccion();

        Transacciones.INSTANCE.nuevaTransaccion(transaccion);
    }
}
