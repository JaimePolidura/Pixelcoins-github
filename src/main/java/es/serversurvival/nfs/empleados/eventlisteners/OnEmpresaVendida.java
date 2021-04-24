package es.serversurvival.nfs.empleados.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.eventos.empresas.EmpresaVendidaEvento;
import es.serversurvival.legacy.mySQL.tablasObjetos.Empleado;

import java.util.List;

public final class OnEmpresaVendida implements AllMySQLTablesInstances {
    @EventListener
    public void onEmpresaVendida (EmpresaVendidaEvento evento) {
        Empleado emplado = empleadosMySQL.getEmpleado(evento.getComprador(), evento.getEmpresa());
        if(emplado != null){
            empleadosMySQL.borrarEmplado(emplado.getId());
        }

        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(evento.getEmpresa());

        empleados.forEach(empleado -> {
            mensajesMySQL.nuevoMensaje("", empleado.getJugador(), "La empresa en la que trabajas " + evento.getEmpresa()
                    + " ha cambiado de owner a " + evento.getComprador());
        });
    }
}
