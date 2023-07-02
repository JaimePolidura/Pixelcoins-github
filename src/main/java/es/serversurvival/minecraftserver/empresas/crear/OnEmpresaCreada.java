package es.serversurvival.minecraftserver.empresas.crear;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.pixelcoins.empresas.crear.EmpresaCreada;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;

import static org.bukkit.ChatColor.*;
import static org.bukkit.Sound.*;

@EventHandler
@AllArgsConstructor
public final class OnEmpresaCreada {
    private final EnviadorMensajes enviadorMensajes;
    private final JugadoresService jugadoresService;

    @EventListener
    public void on(EmpresaCreada e) {
        String jugadorNombre = jugadoresService.getNombreById(e.getEmpresa().getFundadorJugadorId());
        String nombreEmpresa = e.getEmpresa().getNombre();

        enviadorMensajes.enviarMensajeYSonido(e.getEmpresa().getFundadorJugadorId(), ENTITY_PLAYER_LEVELUP, GOLD + "Has creado la empresa " + nombreEmpresa + " Comandos utiles: " +
                AQUA + "/empresas depositar, /empresas contratar, /empresas logotipo, /empresas vertodas, /empresas miempresa "+nombreEmpresa+" /empresas editar");

        MinecraftUtils.broadcastExcept(e.getEmpresa().getFundadorJugadorId(), GOLD + jugadorNombre + " ha creado una nueva empresa: " + DARK_AQUA + nombreEmpresa);
    }
}
