package es.serversurvival.nfs.empleados.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.tablasObjetos.Empleado;
import es.serversurvival.nfs.empresas.editarnombre.EmpresaNombreEditadoEvento;

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
