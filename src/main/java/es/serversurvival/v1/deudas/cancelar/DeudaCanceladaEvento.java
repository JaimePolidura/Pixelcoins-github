package es.serversurvival.v1.deudas.cancelar;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public final class DeudaCanceladaEvento extends PixelcoinsEvento {
    @Getter private final String acredor;
    @Getter private final String deudor;
    @Getter private final double pixelcoinsRestantes;

    public static DeudaCanceladaEvento of(String acredor, String deudor, int pixelcoinsRestantes){
        return new DeudaCanceladaEvento(acredor, deudor, pixelcoinsRestantes);
    }
}
