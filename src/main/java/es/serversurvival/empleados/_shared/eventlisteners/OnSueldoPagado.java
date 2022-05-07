package es.serversurvival.empleados._shared.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.empresas._shared.application.tasks.SueldoPagadoEvento;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

import java.util.Date;

public final class OnSueldoPagado implements AllMySQLTablesInstances {
    @EventListener
    public void onSueldoPagado (SueldoPagadoEvento evento) {
        Date hoy = new Date();

        empleadosMySQL.setFechaPaga(evento.getEmpleadoId(), dateFormater.format(hoy));
    }
}
