package es.serversurvival.mensajes.eventlisteners;

import es.jaime.EventListener;
import es.jaimetruman.annotations.Component;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.ordenespremarket.ejecutarordenes.OrdenNoEjecutadoEvento;
import es.serversurvival.mensajes._shared.application.MensajesService;

@Component
public final class OnOrdenNoEjutada {
    private final MensajesService mensajesService;

    public OnOrdenNoEjutada(){
        this.mensajesService = DependecyContainer.get(MensajesService.class);
    }

    @EventListener
    public void on (OrdenNoEjecutadoEvento e) {
        this.mensajesService.save(e.getJugadorNombre(), "No se ha podido ejecutar la orden de "
                + e.getNombreActivo() + " por que no tenias las suficintes pixelcoins");
    }
}
