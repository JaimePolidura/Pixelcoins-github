package es.serversurvival.empleados.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.empleados.mysql.Empleado;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.empresas.editarnombre.EmpresaNombreEditadoEvento;

import java.util.List;

public final class OnNombreEmpresaEditado implements AllMySQLTablesInstances {
    @EventListener
    public void onNombreEmpresaEditado (EmpresaNombreEditadoEvento evento) {
        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(evento.getNuevoNombre());

        empleados.forEach(empleado -> {
            mensajesMySQL.nuevoMensaje("", empleado.getJugador(), "La empresa en la que trabajas: " + evento.getAntiguoNombre() +
                    " ha cambiado a de nombre a " + evento.getNuevoNombre());
        });
    }
}
