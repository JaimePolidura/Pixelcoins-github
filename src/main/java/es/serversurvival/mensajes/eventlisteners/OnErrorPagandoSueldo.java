package es.serversurvival.mensajes.eventlisteners;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.empresas.empresas.pagarsueldostask.ErrorPagandoSueldo;
import es.serversurvival.mensajes._shared.application.MensajesService;
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
