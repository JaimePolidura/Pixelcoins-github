package es.serversurvival.empleados._shared.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.empleados._shared.mysql.Empleado;
import es.serversurvival.empresas.vender.EmpresaVendedidaEvento;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

import java.util.List;

public final class OnEmpresaVendida implements AllMySQLTablesInstances {
    @EventListener
    public void onEmpresaVendida (EmpresaVendedidaEvento evento) {
        Empleado emplado = empleadosMySQL.getEmpleado(evento.getComprador(), evento.getEmpresaNombre());
        if(emplado != null){
            empleadosMySQL.borrarEmplado(emplado.getId());
        }

        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(evento.getEmpresaNombre());

        empleados.forEach(empleado -> {
            mensajesMySQL.nuevoMensaje("", empleado.getJugador(), "La empresa en la que trabajas " + evento.getEmpresaNombre()
                    + " ha cambiado de owner a " + evento.getComprador());
        });
    }
}
