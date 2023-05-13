package es.serversurvival.mensajes.eventlisteners;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.empresas.empresas.pagarsueldostask.SueldoPagadoEvento;
import es.serversurvival.mensajes._shared.application.MensajesService;
import lombok.AllArgsConstructor;

@EventHandler
@AllArgsConstructor
public final class OnSueldoPagado {
    private final MensajesService mensajesService;

    @EventListener
    public void on(SueldoPagadoEvento evento){
        this.mensajesService.save(evento.getEmpleado(), "Has cobrado " + evento.getSueldo() + " PC de parte de la empresa: " + evento.getEmpresa());
    }
}
