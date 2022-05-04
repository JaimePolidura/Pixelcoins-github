package es.serversurvival.mensajes.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empleados._shared.mysql.Empleado;
import es.serversurvival.empresas.vender.EmpresaVendedidaEvento;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.mensajes._shared.application.MensajesService;

import java.util.List;

public final class OnEmpresaVendida implements AllMySQLTablesInstances {
    private final MensajesService mensajesService;

    public OnEmpresaVendida(){
        this.mensajesService = DependecyContainer.get(MensajesService.class);
    }

    @EventListener
    public void onEmpresaVendida (EmpresaVendedidaEvento evento) {
        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(evento.getEmpresaNombre());

        empleados.forEach(empleado -> {
            mensajesService.save(empleado.getJugador(), "La empresa en la que trabajas " + evento.getEmpresaNombre()
                    + " ha cambiado de owner a " + evento.getComprador());
        });
    }
}
