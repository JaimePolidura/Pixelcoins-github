package es.serversurvival.empresas.empresas.eventlisteners;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.empresas.empleados.irse.EmpleadoDejaEmpresaEvento;
import es.serversurvival.mensajes._shared.application.EnviadorMensajes;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

@EventHandler
@RequiredArgsConstructor
public final class OnEmpleadoDejaEmpresa {
    private final EnviadorMensajes enviadorMensajes;

    @EventListener
    public void onEmpleadoDejaEmpresa (EmpleadoDejaEmpresaEvento evento) {
        String mensajeOnline = ChatColor.RED + evento.getEmpleado() + " Se ha ido de tu empresa: " + evento.getEmpresaNombre();
        String mensajeOffline = evento.getEmpleado() + " se ha ido de tu empresa: " + evento.getEmpresaNombre();

        enviadorMensajes.enviarMensaje(evento.getEmpresaOwner(), mensajeOnline, mensajeOffline, Sound.BLOCK_ANVIL_LAND, 10, 1);
    }
}
