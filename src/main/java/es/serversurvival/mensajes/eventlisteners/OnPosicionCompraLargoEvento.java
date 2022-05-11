package es.serversurvival.mensajes.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.other.comprarlargo.PosicionCompraLargoEvento;
import es.serversurvival.mensajes._shared.application.MensajesService;

public final class OnPosicionCompraLargoEvento {
    private final MensajesService mensajesService;

    public OnPosicionCompraLargoEvento(){
        this.mensajesService = DependecyContainer.get(MensajesService.class);
    }

    @EventListener
    public void on(PosicionCompraLargoEvento evento){
        mensajesService.save(evento.getComprador(), "Se ha ejecutado la orden de: " + evento.getNombreValor() + " -" + evento.getCantidadPosicion() * evento.getPrecioUnidad() + " PC");
    }
}
