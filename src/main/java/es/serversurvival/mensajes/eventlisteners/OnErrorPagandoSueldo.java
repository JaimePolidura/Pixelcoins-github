package es.serversurvival.mensajes.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empresas.pagarsueldostask.ErrorPagandoSueldo;
import es.serversurvival.mensajes._shared.application.MensajesService;

public final class OnErrorPagandoSueldo {
    private final MensajesService mensajesService;

    public OnErrorPagandoSueldo(){
        this.mensajesService = DependecyContainer.get(MensajesService.class);
    }

    @EventListener
    public void on(ErrorPagandoSueldo e){
        this.mensajesService.save(e.getEmpleado(), "No has podido cobrar tu sueldo por parte de " + e.getRazon() + " razon: " + e.getRazon());
    }
}
