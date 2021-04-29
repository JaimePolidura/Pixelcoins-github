package es.serversurvival.mensajes.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.bolsa.ordenespremarket.ejecutarordenes.OrdenNoEjecutadoEvento;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class OnOrdenNoEjutada implements AllMySQLTablesInstances {
    @EventListener
    public void on (OrdenNoEjecutadoEvento e) {
        mensajesMySQL.nuevoMensaje("", e.getJugador(), "No se ha podido ejecutar la orden de "
                + e.getOrden().getNombre_activo() + " por que no tenias las suficintes pixelcoins");
    }
}
