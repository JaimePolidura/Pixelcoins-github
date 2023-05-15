package es.serversurvival.v2.pixelcoins.jugadores._shared;

import lombok.*;

import java.util.UUID;

@ToString
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class Jugador {
    @Getter       private final UUID jugadorId;
    @Getter @With private final String nombre;
}
