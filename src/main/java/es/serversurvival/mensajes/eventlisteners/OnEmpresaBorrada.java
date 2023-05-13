package es.serversurvival.mensajes.eventlisteners;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.empresas.empresas.borrar.EmpresaBorrada;
import es.serversurvival.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;

import java.util.List;

@EventHandler
@AllArgsConstructor
public final class OnEmpresaBorrada {
    private final EnviadorMensajes enviadorMensajes;

    @EventListener
    public void onEmpresaBorrada (EmpresaBorrada evento) {
        List<String> empleados = evento.getAntiguosEmpleadosNombre();

        empleados.forEach( (empleado) -> {
            String mensajeOnline = ChatColor.GOLD + evento.getJugador() + " ha borrado su empresa donde trabajabas: " + evento.getEmpresa();
            String mensajeOffline = "El owner de la empresa en la que trabajas: " + evento.getEmpresa() + " la ha borrado, ya no existe";

            enviadorMensajes.enviarMensaje(empleado, mensajeOnline, mensajeOffline);
        });
    }
}
