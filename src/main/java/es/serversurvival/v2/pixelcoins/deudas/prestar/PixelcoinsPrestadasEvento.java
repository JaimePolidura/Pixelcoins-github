package es.serversurvival.v2.pixelcoins.deudas.prestar;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.v2.pixelcoins.deudas._shared.Deuda;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class PixelcoinsPrestadasEvento extends PixelcoinsEvento {
    @Getter private final Deuda deuda;
}
