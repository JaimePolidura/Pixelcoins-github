package es.serversurvival.v2.pixelcoins.mensajes._shared;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v2.minecraftserver._shared.MinecraftUtils;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class EnviadorMensajes {
    private final MensajesService mensajesService;

    public void enviar(TipoMensaje tipoMensaje, UUID jugadorIdDestinatario, String mensaje) {
        mensajesService.save(Mensaje.builder()
                .mensajeId(UUID.randomUUID())
                .destinatarioId(jugadorIdDestinatario)
                .tipoMensaje(tipoMensaje)
                .fechaEnvio(LocalDateTime.now())
                .mensaje(mensaje)
                .build());
    }
}
