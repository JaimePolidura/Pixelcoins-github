package es.serversurvival.minecraftserver.deudas.prestar;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.deudas.prestar.PixelcoinsPrestadasEvento;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.GOLD;

@EventHandler
@AllArgsConstructor
public final class OnDeudaPrestada {
    private final EnviadorMensajes enviadorMensajes;
    private final JugadoresService jugadoresService;

    @EventListener
    public void on(PixelcoinsPrestadasEvento e) {
        String deudorNombre = jugadoresService.getNombreById(e.getDeuda().getDeudorJugadorId());
        String acredorNombre = jugadoresService.getNombreById(e.getDeuda().getAcredorJugadorId());

        enviadorMensajes.enviarMensaje(e.getDeuda().getDeudorJugadorId(), GOLD + "Has aceptado la solicitud de prestamo de "
                + acredorNombre + " para ver tus deudas " + AQUA + "/deudas ver");

        enviadorMensajes.enviarMensajeYSonido(e.getDeuda().getAcredorJugadorId(), Sound.ENTITY_PLAYER_LEVELUP,
                GOLD + deudorNombre + " te ha aceptado la solicitud de deuda");
    }
}
