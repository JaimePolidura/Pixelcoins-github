package es.serversurvival.minecraftserver.empresas.ipo;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.EventListener;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas.ipo.EmpresaIPORealizada;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.Sound.*;

@Service
@AllArgsConstructor
public final class OnEmpresaIPO {
    private final EnviadorMensajes enviadorMensajes;
    private final JugadoresService jugadoresService;
    private final EmpresasService empresasService;

    @EventListener
    public void on(EmpresaIPORealizada e) {
        Empresa empresa = empresasService.getById(e.getEmpresaId());

        enviadorMensajes.enviarMensajeYSonido(empresa.getDirectorJugadorId(), ENTITY_PLAYER_LEVELUP,
                GOLD + "Has sacado a bolsa la empresa " + empresa.getNombre() + "por "
                        + formatPixelcoins(e.getPrecioPorAccion()) + "/ accion " + AQUA + " /empresas mercado");

        MinecraftUtils.broadcastExcept(empresa.getDirectorJugadorId(), GOLD + jugadoresService.getNombreById(empresa.getDirectorJugadorId())
                + " ha sacado a bolsa la empresa " + empresa.getNombre() + "por "
                + formatPixelcoins(e.getPrecioPorAccion()) + "/ accion " + AQUA + " /empresas mercado");
    }
}
