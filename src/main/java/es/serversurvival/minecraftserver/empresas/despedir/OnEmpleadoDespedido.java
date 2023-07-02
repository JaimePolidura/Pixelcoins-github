package es.serversurvival.minecraftserver.empresas.despedir;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas.despedir.EmpleadoDespedido;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;

import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.RED;

@EventHandler
@AllArgsConstructor
public final class OnEmpleadoDespedido {
    private final JugadoresService jugadoresService;
    private final EnviadorMensajes enviadorMensajes;
    private final EmpresasService empresasService;

    @EventListener
    public void on(EmpleadoDespedido e) {
        String empleadoNombre = jugadoresService.getNombreById(e.getEmpleadoId());
        Empresa empresa = empresasService.getById(e.getEmpresaId());

        enviadorMensajes.enviarMensaje(empresa.getDirectorJugadorId(), GOLD + "Has despedido a: " + empleadoNombre + " de la empresa " + empresa.getNombre());

        enviadorMensajes.enviarMensajeYSonido(e.getEmpleadoId(), Sound.BLOCK_ANVIL_LAND, RED + "Has sido despedido de " + empresa + " por: " +
                e.getCauseDespido());
    }
}
