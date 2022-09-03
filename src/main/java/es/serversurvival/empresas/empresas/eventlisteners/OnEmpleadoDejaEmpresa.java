package es.serversurvival.empresas.empresas.eventlisteners;

import es.jaime.EventListener;
import es.dependencyinjector.annotations.Component;
import es.serversurvival.empresas.empleados.irse.EmpleadoDejaEmpresaEvento;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import static es.serversurvival._shared.utils.Funciones.enviarMensaje;

@Component
public final class OnEmpleadoDejaEmpresa {
    @EventListener
    public void onEmpleadoDejaEmpresa (EmpleadoDejaEmpresaEvento evento) {
        String mensajeOnline = ChatColor.RED + evento.getEmpleado() + " Se ha ido de tu empresa: " + evento.getEmpresaNombre();
        String mensajeOffline = evento.getEmpleado() + " se ha ido de tu empresa: " + evento.getEmpresaNombre();

        enviarMensaje(evento.getEmpresaOwner(), mensajeOnline, mensajeOffline, Sound.BLOCK_ANVIL_LAND, 10, 1);
    }
}
