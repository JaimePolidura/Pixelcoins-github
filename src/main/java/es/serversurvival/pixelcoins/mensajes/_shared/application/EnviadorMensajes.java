package es.serversurvival.pixelcoins.mensajes._shared.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.mensajes._shared.application.MensajesService;
import es.serversurvival.pixelcoins.mensajes._shared.domain.Mensaje;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class EnviadorMensajes {
    private final MensajesService mensajesService;

    public void enviarMensajeYSonido(UUID jugadorId, Sound sound, String mensaje) {
        Player player = Bukkit.getPlayer(jugadorId);

        if(player != null){
            player.sendMessage(mensaje);
            player.playSound(player.getLocation(), sound, 10, 1);
        }else{
            enviarMensajeOffline(jugadorId, mensaje);
        }
    }

    public void enviarMensaje(UUID jugadorId, String mensaje) {
        Player player = Bukkit.getPlayer(jugadorId);

        if (player != null) {
            player.sendMessage(mensaje);
        } else {
            enviarMensajeOffline(jugadorId, mensaje);
        }
    }

    private void enviarMensajeOffline(UUID jugadorId, String mensaje) {
        mensajesService.save(Mensaje.builder()
                .destinatarioId(jugadorId)
                .mensaje(mensaje)
                .build());
    }
}
