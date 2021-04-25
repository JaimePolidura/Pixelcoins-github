package es.serversurvival.nfs.empleados.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.nfs.empresas.tasks.SueldoPagadoEvento;

import java.util.Date;

public final class OnSueldoPagado implements AllMySQLTablesInstances {
    @EventListener
    public void onSueldoPagado (SueldoPagadoEvento evento) {
        Date hoy = new Date();

        empleadosMySQL.setFechaPaga(evento.getEmpleadoId(), dateFormater.format(hoy));
    }
}
