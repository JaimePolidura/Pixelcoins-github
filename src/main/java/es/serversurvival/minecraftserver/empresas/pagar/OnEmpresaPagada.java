package es.serversurvival.minecraftserver.empresas.pagar;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.empresas._shared.empleados.application.EmpleadosService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas.pagar.EmpresaPagada;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;

import static es.serversurvival._shared.utils.Funciones.formatPixelcoins;
import static org.bukkit.ChatColor.GOLD;

@EventHandler
@AllArgsConstructor
public final class OnEmpresaPagada {
    private final EmpleadosService empleadosService;
    private final EnviadorMensajes enviadorMensajes;
    private final JugadoresService jugadoresService;
    private final EmpresasService empresasService;

    @EventListener
    public void on(EmpresaPagada e) {
        String pagadorNombre = jugadoresService.getNombreById(e.getJugadorPagadorId());
        Empresa empresa = empresasService.getById(e.getEmpresaId());

        enviadorMensajes.enviarMensaje(e.getJugadorPagadorId(), GOLD + "Has pagado a la empresa " +
                empresa.getNombre() + " " + formatPixelcoins(e.getPixelcoins()));

        empleadosService.findEmpleoActivoByEmpresaId(empresa.getEmpresaId()).forEach(empleado -> {
            enviadorMensajes.enviarMensajeYSonido(empleado.getEmpleadoJugadorId(), Sound.ENTITY_PLAYER_LEVELUP, GOLD + pagadorNombre +
                    " ha pagado " + formatPixelcoins(e.getPixelcoins()) + " a la empresa donde trabajas " + empresa.getNombre());
        });
    }
}
