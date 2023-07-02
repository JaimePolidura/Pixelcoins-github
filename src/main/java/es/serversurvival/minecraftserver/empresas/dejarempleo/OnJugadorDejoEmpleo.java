package es.serversurvival.minecraftserver.empresas.dejarempleo;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas.dejarempleo.JugadorDejoEmpleo;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;

import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.RED;

@EventHandler
@AllArgsConstructor
public final class OnJugadorDejoEmpleo {
    private final JugadoresService jugadoresService;
    private final EnviadorMensajes enviadorMensajes;
    private final EmpresasService empresasService;

    @EventListener
    public void on(JugadorDejoEmpleo e) {
        String nombreJugador = jugadoresService.getNombreById(e.getJugadorId());
        Empresa empresa = empresasService.getById(e.getEmpresaId());

        enviadorMensajes.enviarMensaje(e.getJugadorId(), GOLD + "Te has ido del trabajo de " + empresa.getNombre());
        enviadorMensajes.enviarMensaje(empresa.getDirectorJugadorId(), RED + nombreJugador + " se ha ido de la empresa " + empresa.getNombre());
    }
}
