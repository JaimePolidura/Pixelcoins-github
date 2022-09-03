package es.serversurvival.mensajes.eventlisteners;

import es.jaime.EventListener;
import es.dependencyinjector.annotations.Component;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbiertaEvento;
import es.serversurvival.mensajes._shared.application.MensajesService;

@Component
public final class OnPosicionCompraLargoEvento {
    private final MensajesService mensajesService;

    public OnPosicionCompraLargoEvento(){
        this.mensajesService = DependecyContainer.get(MensajesService.class);
    }

    @EventListener
    public void on(PosicionAbiertaEvento evento){
        mensajesService.save(evento.getComprador(), "Se ha ejecutado la orden de: " + evento.getNombreActivoLargo() + " -" + evento.getCantidadPosicion() * evento.getPrecioUnidad() + " PC");
    }
}
