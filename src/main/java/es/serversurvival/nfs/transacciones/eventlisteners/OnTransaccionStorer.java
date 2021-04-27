package es.serversurvival.nfs.transacciones.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.nfs.shared.eventospixelcoins.EventoTipoTransaccion;
import es.serversurvival.nfs.shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.nfs.transacciones.mySQL.Transaccion;

public final class OnTransaccionStorer {
    @EventListener({EventoTipoTransaccion.class})
    public void onTransaccion (PixelcoinsEvento evento) {
        Transaccion transaccion = ((EventoTipoTransaccion) evento).buildTransaccion();

        Transacciones.INSTANCE.nuevaTransaccion(transaccion);
    }
}
