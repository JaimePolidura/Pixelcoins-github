package es.serversurvival.v1.mensajes.eventlisteners;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.v1.empresas.empresas.pagarsueldostask.ErrorPagandoSueldo;
import es.serversurvival.v1.mensajes._shared.application.MensajesService;
import lombok.AllArgsConstructor;

@EventHandler
@AllArgsConstructor
public final class OnErrorPagandoSueldo {
    private final MensajesService mensajesService;

    @EventListener
    public void on(ErrorPagandoSueldo e){
        this.mensajesService.save(e.getEmpleado(), "No has podido cobrar tu sueldo por parte de " + e.getRazon() + " razon: " + e.getRazon());
    }
}
