package es.serversurvival.empleados._shared.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.empleados._shared.mysql.Empleado;
import es.serversurvival.empresas.borrar.EmpresaBorradaEvento;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival._shared.utils.Funciones;
import org.bukkit.ChatColor;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.enviarMensaje;

public final class OnEmpresaBorrada implements AllMySQLTablesInstances {
    @EventListener
    public void onEmpresaBorrada (EmpresaBorradaEvento evento) {
        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(evento.getEmpresa());

        empleados.forEach( (empleado) -> {
            String mensajeOnline = ChatColor.GOLD + evento.getJugador() + " ha borrado su empresa donde trabajabas: " + evento.getEmpresa();
            String mensajeOffline = "El owner de la empresa en la que trabajas: " + empleado.getEmpresa() + " la ha borrado, ya no existe";

            Funciones.enviarMensaje(empleado.getJugador(), mensajeOnline, mensajeOffline);
        });
    }
}
