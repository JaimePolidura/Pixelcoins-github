package es.serversurvival.mensajes._shared.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.bolsa._shared.ordenespremarket.ejecutarordenes.OrdenNoEjecutadoEvento;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class OnOrdenNoEjutada implements AllMySQLTablesInstances {
    @EventListener
    public void on (OrdenNoEjecutadoEvento e) {
        mensajesMySQL.nuevoMensaje("", e.getJugador(), "No se ha podido ejecutar la orden de "
                + e.getOrden().getNombre_activo() + " por que no tenias las suficintes pixelcoins");
    }
}
