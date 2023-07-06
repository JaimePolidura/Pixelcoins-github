package es.serversurvival.minecraftserver.empresas.cerrar;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.empresas._shared.empleados.application.EmpleadosService;
import es.serversurvival.pixelcoins.empresas.cerrar.EmpresaCerrada;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;

@EventHandler
@AllArgsConstructor
public final class OnEmpresaCerrada {
    private final EnviadorMensajes enviadorMensajes;
    private final EmpleadosService empleadosService;

    @EventListener
    public void on(EmpresaCerrada evento) {
        enviadorMensajes.enviarMensaje(evento.getEmpresa().getDirectorJugadorId(), ChatColor.GOLD + "Has cerrado la empresa, " +
                "se te han embolsado las pixelcoins");

        empleadosService.findEmpleoByEmpresaId(evento.getEmpresa().getEmpresaId()).forEach(empleado -> {
            if(!empleado.getEmpleadoJugadorId().equals(evento.getEmpresa().getDirectorJugadorId())){
                enviadorMensajes.enviarMensaje(empleado.getEmpleadoJugadorId(), ChatColor.GOLD + "Se ha cerrado la empresa " +
                        evento.getEmpresa().getNombre() + " en la que trabajabas");
            }
        });
    }
}
