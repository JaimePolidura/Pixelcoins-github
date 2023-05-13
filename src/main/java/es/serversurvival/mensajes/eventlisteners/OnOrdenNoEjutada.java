package es.serversurvival.mensajes.eventlisteners;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.bolsa.ordenespremarket.ejecutarordenes.OrdenNoEjecutadoEvento;
import es.serversurvival.mensajes._shared.application.MensajesService;
import lombok.AllArgsConstructor;

@EventHandler
@AllArgsConstructor
public final class OnOrdenNoEjutada {
    private final MensajesService mensajesService;

    @EventListener
    public void on (OrdenNoEjecutadoEvento e) {
        this.mensajesService.save(e.getJugadorNombre(), "No se ha podido ejecutar la orden de "
                + e.getNombreActivo() + " por que no tenias las suficintes pixelcoins");
    }
}
