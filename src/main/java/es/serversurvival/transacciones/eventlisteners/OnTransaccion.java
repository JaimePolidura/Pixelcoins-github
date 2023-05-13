package es.serversurvival.transacciones.eventlisteners;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.transacciones._shared.domain.EventoTipoTransaccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.transacciones._shared.application.TransaccionesService;
import es.serversurvival.transacciones._shared.domain.Transaccion;
import lombok.AllArgsConstructor;

@EventHandler
@AllArgsConstructor
public final class OnTransaccion {
    private final TransaccionesService transaccionesService;

    @EventListener({EventoTipoTransaccion.class})
    public void onTransaccion (PixelcoinsEvento evento) {
        Transaccion transaccion = ((EventoTipoTransaccion) evento).buildTransaccion();

        this.transaccionesService.save(transaccion);
    }
}
