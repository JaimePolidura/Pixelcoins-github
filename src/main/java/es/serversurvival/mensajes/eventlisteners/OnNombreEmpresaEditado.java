package es.serversurvival.mensajes.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empleados._shared.mysql.Empleado;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.empresas.editarnombre.EmpresaNombreEditadoEvento;
import es.serversurvival.mensajes._shared.application.MensajesService;

import java.util.List;

public final class OnNombreEmpresaEditado implements AllMySQLTablesInstances {
    private final MensajesService mensajesService;

    public OnNombreEmpresaEditado(){
        this.mensajesService = DependecyContainer.get(MensajesService.class);
    }

    @EventListener
    public void onNombreEmpresaEditado (EmpresaNombreEditadoEvento evento) {
        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(evento.getNuevoNombre());

        empleados.forEach(empleado -> {
            mensajesService.save(empleado.getJugador(), "La empresa en la que trabajas: " + evento.getAntiguoNombre() +
                    " ha cambiado a de nombre a " + evento.getNuevoNombre());
        });
    }
}
