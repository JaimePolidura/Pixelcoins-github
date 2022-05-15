package es.serversurvival.mensajes._shared.domain;

import es.jaime.javaddd.domain.Aggregate;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class Mensaje extends Aggregate {
    @Getter private final UUID id;
    @Getter private final String enviador;
    @Getter private final String destinatario;
    @Getter private final String mensaje;

    public Mensaje withDestinatario(String destinatario){
        return new Mensaje(id, enviador, destinatario, mensaje);
    }
}
