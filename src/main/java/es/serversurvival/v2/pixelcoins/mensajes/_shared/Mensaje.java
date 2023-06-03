package es.serversurvival.v2.pixelcoins.mensajes._shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.UUID;

@Builder
@AllArgsConstructor
public final class Mensaje {
    @Getter private final UUID mensajeId;
    @Getter private final UUID destinatarioId;
    @Getter private final TipoMensaje tipoMensaje;
    @Getter private final LocalDateTime fechaEnvio;
    @Getter private final LocalDateTime fechaVisto;
    @Getter private final String mensaje;

    public Mensaje marcarComoVisto() {
        return new Mensaje(mensajeId, destinatarioId, tipoMensaje, fechaEnvio, LocalDateTime.now(), mensaje);
    }

    public boolean haSidoVisto() {
        return this.fechaVisto != null;
    }

    public static Comparator<Mensaje> sortByMostRecentFechaEnvio() {
        return (a, b) -> b.fechaEnvio.compareTo(a.fechaEnvio);
    }
}
