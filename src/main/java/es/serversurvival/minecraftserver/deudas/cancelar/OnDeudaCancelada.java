package es.serversurvival.minecraftserver.deudas.cancelar;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.deudas.cancelar.DeudaCancelada;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;

import static es.serversurvival._shared.utils.Funciones.formatPixelcoins;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.Sound.*;

@EventHandler
@AllArgsConstructor
public final class OnDeudaCancelada {
    private final EnviadorMensajes enviadorMensajes;
    private final JugadoresService jugadoresService;

    @EventListener
    public void on(DeudaCancelada evento) {
        Deuda deuda = evento.getDeuda();
        String nombreAcredor = jugadoresService.getNombreById(deuda.getAcredorJugadorId());
        String pixelcoinsDeuda = formatPixelcoins(evento.getDeuda().getPixelcoinsRestantesDePagar());

        enviadorMensajes.enviarMensaje(evento.getDeuda().getAcredorJugadorId(), GOLD + "Has cancelado la deuda");
        enviadorMensajes.enviarMensajeYSonido(deuda.getDeudorJugadorId(), ENTITY_PLAYER_LEVELUP, nombreAcredor +
                " te ha cancelado la deuda que tenia contigo por " + pixelcoinsDeuda);
    }
}
