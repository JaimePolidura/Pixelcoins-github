package es.serversurvival.v1.mensajes._shared.application;

import es.dependencyinjector.dependencies.annotations.Service;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@Service
@AllArgsConstructor
public final class EnviadorMensajes {
    private final MensajesService mensajesService;

    public void enviarMensaje (String nombreJugador, String mensajeOnline, String mensajeOffline, Sound sound, int v1, int v2) {
        Player player = Bukkit.getPlayer(nombreJugador);
        if (player != null) {
            player.sendMessage(mensajeOnline);
            player.playSound(player.getLocation(), sound, v1, v2);
        } else {
            this.mensajesService.save(nombreJugador, mensajeOffline);
        }
    }

    public void enviarMensajeYSonidoSiOnline(String jugador, String mensaje, Sound sound) {
        Player player = Bukkit.getPlayer(jugador);
        if(player != null){
            player.sendMessage(mensaje);
            player.playSound(player.getLocation(), sound, 10, 1);
        }
    }

    public void enviarMensajeYSonido (Player player, String mensaje, Sound sound) {
        player.sendMessage(mensaje);
        player.playSound(player.getLocation(), sound, 10, 1);
    }

    public void enviarMensaje (String nombreJugador, String mensajeOnline, String mensajeOffline) {
        Player player = Bukkit.getPlayer(nombreJugador);
        if(player != null){
            player.sendMessage(mensajeOnline);
        }else{
            this.mensajesService.save(nombreJugador, mensajeOffline);
        }
    }
}
