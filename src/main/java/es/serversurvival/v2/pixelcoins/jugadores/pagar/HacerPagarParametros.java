package es.serversurvival.v2.pixelcoins.jugadores.pagar;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class HacerPagarParametros {
    @Getter private final UUID pagadorId;
    @Getter private final UUID pagadoId;
    @Getter private final double pixelcions;
}
