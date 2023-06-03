package es.serversurvival.v1.mensajes._shared.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Comparator;
import java.util.UUID;

@AllArgsConstructor
public final class Mensaje {
    public static final long TIEMPO_MAXIMO_MENSAJE_MS = 7L * 24 * 60 * 60 * 1000;

    @Getter private final UUID mensajeId;
    @Getter private final String enviador;
    @Getter private final String destinatario;
    @Getter private final String mensaje;

    public Mensaje withDestinatario(String destinatario){
        return new Mensaje(mensajeId, enviador, destinatario, mensaje);
    }
}
