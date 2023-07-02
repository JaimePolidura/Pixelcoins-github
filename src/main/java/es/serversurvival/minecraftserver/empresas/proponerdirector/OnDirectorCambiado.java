package es.serversurvival.minecraftserver.empresas.proponerdirector;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas.cambiardirector.votacionfinalizada.DirectorCambiado;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@EventHandler
@AllArgsConstructor
public final class OnDirectorCambiado {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final JugadoresService jugadoresService;
    private final EnviadorMensajes enviadorMensajes;
    private final EmpresasService empresasService;

    @EventListener
    public void on(DirectorCambiado e) {
        String antiguoDirectorNombre = jugadoresService.getNombreById(e.getAntiguoDirectorJugadorId());
        String nuevoDirectorNombre = jugadoresService.getNombreById(e.getNuevoDirectorJugadorId());
        Empresa empresa = empresasService.getById(e.getEmpresaId());

        enviadorMensajes.enviarMensaje(e.getAntiguoDirectorJugadorId(), GOLD + "Has sido despedido por los de la empresa "
                + empresa.getNombre() + " por " + e.getRazonDespido());
        enviadorMensajes.enviarMensaje(e.getNuevoDirectorJugadorId(), GOLD + "Has sido contratado por los accionistas en la empresa " +
                empresa.getNombre() + " por " + formatPixelcoins(e.getNuevoSueldo()) + "/" + millisToDias(e.getPeriodoPagoMs()) + " dias");

        accionistasEmpresasService.findByEmpresaId(e.getEmpresaId()).forEach(accionista -> {
            enviadorMensajes.enviarMensaje(accionista.getAccionisaJugadorId(), GOLD + "La propuesta de cambiar de director de " + antiguoDirectorNombre + " a "
                    + nuevoDirectorNombre + " ha sido aceptado. Ahora el nuevo director es " + nuevoDirectorNombre);
        });
    }
}
