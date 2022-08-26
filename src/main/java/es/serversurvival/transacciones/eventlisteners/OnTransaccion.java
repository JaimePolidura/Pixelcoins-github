package es.serversurvival.transacciones.eventlisteners;

import es.jaime.EventListener;
import es.jaimetruman.annotations.Component;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.transacciones._shared.application.TransaccionesService;
import es.serversurvival.transacciones._shared.domain.Transaccion;

@Component
public final class OnTransaccion {
    private final TransaccionesService transaccionesService;

    public OnTransaccion(){
        this.transaccionesService = DependecyContainer.get(TransaccionesService.class);
    }

    @EventListener({EventoTipoTransaccion.class})
    public void onTransaccion (PixelcoinsEvento evento) {
        Transaccion transaccion = ((EventoTipoTransaccion) evento).buildTransaccion();

        this.transaccionesService.save(transaccion);
    }
}
