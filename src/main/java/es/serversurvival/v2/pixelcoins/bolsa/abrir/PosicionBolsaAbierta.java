package es.serversurvival.v2.pixelcoins.bolsa.abrir;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class PosicionBolsaAbierta extends PixelcoinsEvento {
    @Getter private final UUID jugadorId;
}
