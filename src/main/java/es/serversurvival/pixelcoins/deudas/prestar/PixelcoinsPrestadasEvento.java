package es.serversurvival.pixelcoins.deudas.prestar;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.deudas._shared.Deuda;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class PixelcoinsPrestadasEvento extends PixelcoinsEvento {
    @Getter private final Deuda deuda;
}
