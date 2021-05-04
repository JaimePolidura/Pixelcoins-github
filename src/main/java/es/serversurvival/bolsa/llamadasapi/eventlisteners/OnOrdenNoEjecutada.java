package es.serversurvival.bolsa.llamadasapi.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.bolsa.ordenespremarket.ejecutarordenes.OrdenNoEjecutadoEvento;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class OnOrdenNoEjecutada implements AllMySQLTablesInstances {
    @EventListener
    public void onOrdenNoEjecutada (OrdenNoEjecutadoEvento evento) {
        llamadasApiMySQL.borrarLlamadaSiNoEsUsada(evento.getOrden().getNombre_activo());
    }
}
