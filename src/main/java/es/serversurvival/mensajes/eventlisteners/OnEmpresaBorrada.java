package es.serversurvival.mensajes.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empresas.borrar.EmpresaBorradaEvento;
import org.bukkit.ChatColor;

import java.util.List;

public final class OnEmpresaBorrada {
    @EventListener
    public void onEmpresaBorrada (EmpresaBorradaEvento evento) {
        List<String> empleados = evento.getAntiguosEmpleadosNombre();

        empleados.forEach( (empleado) -> {
            String mensajeOnline = ChatColor.GOLD + evento.getJugador() + " ha borrado su empresa donde trabajabas: " + evento.getEmpresa();
            String mensajeOffline = "El owner de la empresa en la que trabajas: " + evento.getEmpresa() + " la ha borrado, ya no existe";

            Funciones.enviarMensaje(empleado, mensajeOnline, mensajeOffline);
        });
    }
}
