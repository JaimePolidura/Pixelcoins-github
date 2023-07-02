package es.serversurvival.pixelcoins.deudas.pagartodo;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class DeudaPagadoPorCompleto extends PixelcoinsEvento {
    @Getter private final UUID deudaId;
    @Getter private final UUID deudorJugadorId;
    @Getter private final double totalCoste;
}
