package es.serversurvival.pixelcoins.jugadores._shared.jugadores;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class Jugador {
    @Getter       private UUID jugadorId;
    @Getter @With private String nombre;
    @Getter       private LocalDateTime fechaCreacion;
}
