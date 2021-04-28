package es.serversurvival.empresas.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.empleados.irse.EmpleadoDejaEmpresaEvento;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import static es.serversurvival.utils.Funciones.enviarMensaje;

public final class OnEmpleadoDejaEmpresa {
    @EventListener
    public void onEmpleadoDejaEmpresa (EmpleadoDejaEmpresaEvento evento) {
        String mensajeOnline = ChatColor.RED + evento.getEmpleado() + " Se ha ido de tu empresa: " + evento.getEmpresa().getNombre();
        String mensajeOffline = evento.getEmpleado() + " se ha ido de tu empresa: " + evento.getEmpresa();

        enviarMensaje(evento.getEmpresa().getOwner(), mensajeOnline, mensajeOffline, Sound.BLOCK_ANVIL_LAND, 10, 1);
    }
}
