package es.serversurvival.nfs.transacciones.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.legacy.mySQL.eventos.EventoTipoTransaccion;
import es.serversurvival.legacy.mySQL.eventos.PixelcoinsEvento;
import es.serversurvival.nfs.transacciones.mySQL.Transaccion;

public final class OnTransaccionStorer {
    @EventListener({EventoTipoTransaccion.class})
    public void onTransaccion (PixelcoinsEvento evento) {
        Transaccion transaccion = ((EventoTipoTransaccion) evento).buildTransaccion();

        Transacciones.INSTANCE.nuevaTransaccion(transaccion);
    }
}
