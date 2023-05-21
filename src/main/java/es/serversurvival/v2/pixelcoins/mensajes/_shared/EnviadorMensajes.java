package es.serversurvival.v2.pixelcoins.mensajes._shared;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v2.pixelcoins.mensajes._shared.mensajes.Mensaje;
import es.serversurvival.v2.pixelcoins.mensajes._shared.mensajes.MensajesService;
import es.serversurvival.v2.pixelcoins.mensajes._shared.tipomensajes.TipoContenidoMensaje;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class EnviadorMensajes {
    private final MensajesService mensajesService;

    public void enviar(UUID jugadorIdDestinatario, TipoContenidoMensaje tipoContenido, String mensaje, int tipoMensajeId) {
        Player player = Bukkit.getPlayer(jugadorIdDestinatario);

        if(player == null){
            mensajesService.save(Mensaje.builder()
                    .mensajeId(UUID.randomUUID())
                    .destinatarioId(jugadorIdDestinatario)
                    .tipoMensajeId(tipoMensajeId)
                    .fechaEnvio(LocalDateTime.now())
                    .mensaje(mensaje)
                    .build());
        }else{
            player.sendMessage(tipoContenido.getColor() + mensaje);
            player.playSound(player, tipoContenido.getSound(), 10, 1);
        }
    }
}
