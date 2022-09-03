package es.serversurvival.mensajes.eventlisteners;

import es.jaime.EventListener;
import es.dependencyinjector.annotations.Component;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empresas.pagarsueldostask.SueldoPagadoEvento;
import es.serversurvival.mensajes._shared.application.MensajesService;

@Component
public final class OnSueldoPagado {
    private final MensajesService mensajesService;

    public OnSueldoPagado(){
        this.mensajesService = DependecyContainer.get(MensajesService.class);
    }

    @EventListener
    public void on(SueldoPagadoEvento evento){
        this.mensajesService.save(evento.getEmpleado(), "Has cobrado " + evento.getSueldo() + " PC de parte de la empresa: " + evento.getEmpresa());
    }
}
