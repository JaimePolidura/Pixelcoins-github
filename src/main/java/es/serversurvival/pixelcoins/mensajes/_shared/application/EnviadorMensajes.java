package es.serversurvival.pixelcoins.mensajes._shared.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.mensajes._shared.domain.Mensaje;
import es.serversurvival.pixelcoins.mensajes._shared.domain.TipoMensaje;
import lombok.AllArgsConstructor;

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
                .tipo(tipoMensaje)
                .fechaEnvio(LocalDateTime.now())
                .mensaje(mensaje)
                .build());
    }
}
