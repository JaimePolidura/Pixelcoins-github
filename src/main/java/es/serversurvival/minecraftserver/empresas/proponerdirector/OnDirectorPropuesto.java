package es.serversurvival.minecraftserver.empresas.proponerdirector;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas.cambiardirector.proponer.NuevoDirectorPropuesto;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.Sound.ENTITY_PLAYER_LEVELUP;

@EventHandler
@AllArgsConstructor
public final class OnDirectorPropuesto {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final EnviadorMensajes enviadorMensajes;
    private final JugadoresService jugadoresService;
    private final EmpresasService empresasService;

    @EventListener
    public void on(NuevoDirectorPropuesto e) {
        String jugadorNombre = jugadoresService.getNombreById(e.getJugadorId());
        String nombreNuevoDirector = jugadoresService.getNombreById(e.getNuevoDirectorId());
        Empresa empresa = empresasService.getById(e.getEmpresaId());

        enviadorMensajes.enviarMensajeYSonido(e.getJugadorId(), ENTITY_PLAYER_LEVELUP, GOLD + "Has propuesto como director a " + nombreNuevoDirector +
                " de la empresa " + empresa.getNombre() + ". Ahora el resto de accionistas tendran que votar a favor. " +
                "Para ver las votaciones " + AQUA + "/empresas votaciones " + empresa.getNombre());

        accionistasEmpresasService.findByEmpresaId(e.getEmpresaId()).forEach(accionista -> {
            enviadorMensajes.enviarMensaje(accionista.getAccionisaJugadorId(), "En la empresa donde eres accionista "+empresa.getNombre() +
                    " " + jugadorNombre + "ha propuesto como director a "+nombreNuevoDirector+" con la razon de cambio: " + e.getRazonCambio() +
                    ". Ahora tendras que votar "  + AQUA + "/empresas votaciones " + empresa.getNombre());
        });
    }
}
