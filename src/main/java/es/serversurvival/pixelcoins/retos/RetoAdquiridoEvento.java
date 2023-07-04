package es.serversurvival.pixelcoins.retos;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.Reto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class RetoAdquiridoEvento extends PixelcoinsEvento {
    @Getter private final UUID jugadorId;
    @Getter private final Reto reto;
}
