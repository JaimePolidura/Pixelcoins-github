package es.serversurvival.pixelcoins.mensajes._shared.domain;

import es.serversurvival._shared.utils.Funciones;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public final class Mensaje {
    @Getter private UUID mensajeId;
    @Getter private UUID destinatarioId;
    @Getter private LocalDateTime fechaEnvio;
    @Getter private LocalDateTime fechaVisto;
    @Getter private String mensaje;
    @Getter private boolean visto;

    public Mensaje marcarComoVisto() {
        return new Mensaje(mensajeId, destinatarioId, fechaEnvio, LocalDateTime.now(), mensaje, true);
    }

    public static Comparator<Mensaje> sortByMostRecentFechaEnvio() {
        return (a, b) -> b.fechaEnvio.compareTo(a.fechaEnvio);
    }

    public static Mensaje.Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID mensajeId;
        private UUID destinatarioId;
        private LocalDateTime fechaEnvio;
        private LocalDateTime fechaVisto;
        private String mensaje;

        public Builder() {
            this.mensajeId = UUID.randomUUID();
            this.fechaEnvio = LocalDateTime.now();
            this.fechaVisto = Funciones.NULL_LOCALDATETIME;
        }

        public Builder destinatarioId(UUID destinatarioId) {
            this.destinatarioId = destinatarioId;
            return this;
        }

        public Builder mensaje(String mensaje) {
            this.mensaje = mensaje;
            return this;
        }

        public Mensaje build() {
            return new Mensaje(mensajeId, destinatarioId, fechaEnvio, fechaVisto, mensaje, false);
        }
    }
}
