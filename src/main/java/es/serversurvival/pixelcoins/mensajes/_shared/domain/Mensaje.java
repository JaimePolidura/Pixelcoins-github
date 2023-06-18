package es.serversurvival.pixelcoins.mensajes._shared.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class Mensaje {
    public static final long TIEMPO_MAXIMO_MENSAJE_MS = 7L * 24 * 60 * 60 * 1000;

    @Getter private UUID mensajeId;
    @Getter private UUID destinatarioId;
    @Getter private TipoMensaje tipo;
    @Getter private LocalDateTime fechaEnvio;
    @Getter private LocalDateTime fechaVisto;
    @Getter private String mensaje;

    public Mensaje marcarComoVisto() {
        return new Mensaje(mensajeId, destinatarioId, tipo, fechaEnvio, LocalDateTime.now(), mensaje);
    }

    public boolean haSidoVisto() {
        return this.fechaVisto != null;
    }

    public static Comparator<Mensaje> sortByMostRecentFechaEnvio() {
        return (a, b) -> b.fechaEnvio.compareTo(a.fechaEnvio);
    }
}
