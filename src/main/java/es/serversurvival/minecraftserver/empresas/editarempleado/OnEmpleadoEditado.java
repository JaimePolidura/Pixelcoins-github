package es.serversurvival.minecraftserver.empresas.editarempleado;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas.editarempleado.EmpleadoEditado;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;

import static es.serversurvival._shared.utils.Funciones.*;

@EventHandler
@AllArgsConstructor
public final class OnEmpleadoEditado {
    private final EnviadorMensajes enviadorMensajes;
    private final JugadoresService jugadoresService;
    private final EmpresasService empresasService;

    @EventListener
    public void on(EmpleadoEditado e) {
        String empleadoNombre = jugadoresService.getNombreById(e.getEmpleado().getEmpleadoJugadorId());
        Empresa empresa = empresasService.getById(e.getEmpresaId());

        enviadorMensajes.enviarMensaje(empresa.getDirectorJugadorId(), ChatColor.GOLD + "Has editado a " +
                empleadoNombre + " de la empresa " + empresa.getNombre());
        enviadorMensajes.enviarMensaje(e.getEmpleado().getEmpleadoJugadorId(), ChatColor.GOLD + "Se ha editado tus " +
                "condiciones en la empresa. Ahora cobras " + formatPixelcoins(e.getNuevoSueldo()) + "/ " + millisToDias(e.getNuevoPeriodoPago())
                + " dias Y tu nuevo cargo: " + e.getNuevaDescripccion());
    }
}
