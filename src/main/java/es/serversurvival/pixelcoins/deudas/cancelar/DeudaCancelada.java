package es.serversurvival.pixelcoins.deudas.cancelar;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class DeudaCancelada extends PixelcoinsEvento {
    @Getter private Deuda deuda;
}
