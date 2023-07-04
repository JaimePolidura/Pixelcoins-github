package es.serversurvival.minecraftserver.retos;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import es.serversurvival.pixelcoins.retos.RetoAdquiridoEvento;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.recompensas.TipoRecompensa;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

@EventHandler
@AllArgsConstructor
public final class OnRetoAdquirido {
    private final EnviadorMensajes enviadorMensajes;

    @EventListener
    public void on(RetoAdquiridoEvento evento) {
        String mensaje = ChatColor.DARK_PURPLE + "Has alcanzado el reto " + evento.getReto().getNombre();

        if(evento.getReto().getTipoRecompensa() == TipoRecompensa.PIXELCOINS){
            mensaje += " Has sido recompensado con " + Funciones.formatPixelcoins(evento.getReto().getRecomponsaPixelcoins());
        }

        enviadorMensajes.enviarMensajeYSonido(
                evento.getJugadorId(),
                Sound.ENTITY_PLAYER_LEVELUP,
                mensaje
        );
    }
}
