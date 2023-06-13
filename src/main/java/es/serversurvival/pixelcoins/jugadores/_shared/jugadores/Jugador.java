package es.serversurvival.pixelcoins.jugadores._shared.jugadores;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@ToString
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class Jugador {
    @Getter       private final UUID jugadorId;
    @Getter @With private final String nombre;
    @Getter       private final LocalDateTime fechaCreacion;
}
