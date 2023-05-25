package es.serversurvival.v2.pixelcoins.mensajes._shared;

import es.serversurvival.v2.pixelcoins.mensajes._shared.TipoMensaje;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
public final class Mensaje {
    @Getter private final UUID mensajeId;
    @Getter private final UUID destinatarioId;
    @Getter private final TipoMensaje tipoMensaje;
    @Getter private final LocalDateTime fechaEnvio;
    @Getter private final LocalDateTime fechaVista;
    @Getter private final String mensaje;

    public boolean haSidoVisto() {
        return this.fechaVista != null;
    }
}
