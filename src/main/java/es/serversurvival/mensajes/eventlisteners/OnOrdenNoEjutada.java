package es.serversurvival.mensajes.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa._shared.ordenespremarket.ejecutarordenes.OrdenNoEjecutadoEvento;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.mensajes._shared.application.MensajesService;

public final class OnOrdenNoEjutada implements AllMySQLTablesInstances {
    private final MensajesService mensajesService;

    public OnOrdenNoEjutada(){
        this.mensajesService = DependecyContainer.get(MensajesService.class);
    }

    @EventListener
    public void on (OrdenNoEjecutadoEvento e) {
        this.mensajesService.save(e.getJugador(), "No se ha podido ejecutar la orden de "
                + e.getOrden().getNombre_activo() + " por que no tenias las suficintes pixelcoins");
    }
}
