package es.serversurvival.bolsa.other._shared.llamadasapi.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.bolsa.other._shared.ordenespremarket.ejecutarordenes.OrdenNoEjecutadoEvento;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class OnOrdenNoEjecutada implements AllMySQLTablesInstances {
    @EventListener
    public void onOrdenNoEjecutada (OrdenNoEjecutadoEvento evento) {
        llamadasApiMySQL.borrarLlamadaSiNoEsUsada(evento.getOrden().getNombreActivo());
    }
}
