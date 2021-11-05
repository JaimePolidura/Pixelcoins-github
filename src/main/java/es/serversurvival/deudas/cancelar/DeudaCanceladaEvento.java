package es.serversurvival.deudas.cancelar;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class DeudaCanceladaEvento extends PixelcoinsEvento {
    @Getter private final String acredor;
    @Getter private final String deudor;
    @Getter private final int pixelcoins_restantes;
}
