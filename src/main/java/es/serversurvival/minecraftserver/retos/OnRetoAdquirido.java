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

import static org.bukkit.ChatColor.*;

@EventHandler
@AllArgsConstructor
public final class OnRetoAdquirido {
    private final EnviadorMensajes enviadorMensajes;

    @EventListener
    public void on(RetoAdquiridoEvento evento) {
        String mensaje = GOLD + "Has alcanzado el reto \"" + evento.getReto().getNombre() + "\" ";

        if(evento.getReto().esTipoProgresivo()){
            double cantidad = evento.getReto().getCantidadRequerida();

            mensaje += "con " + evento.getReto().getFormatoCantidadRequerida().formatear(cantidad) + " en " +
                    evento.getReto().getNombreUnidadCantidadRequerida().toLowerCase() + " ";
        }
        if(evento.getReto().getTipoRecompensa() == TipoRecompensa.PIXELCOINS){
            mensaje += "Has sido recompensado con " + Funciones.formatPixelcoins(evento.getReto().getRecompensaPixelcoins());
        }

        enviadorMensajes.enviarMensajeYSonido(
                evento.getJugadorId(),
                Sound.ENTITY_PLAYER_LEVELUP,
                mensaje
        );
    }
}
