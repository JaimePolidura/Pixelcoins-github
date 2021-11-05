package es.serversurvival.deudas.pagarCuotas;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class DeudaCuotaNoPagadaEvento extends PixelcoinsEvento {
    @Getter private final String acredor;
    @Getter private final String deudor;
    @Getter private final int deudaId;
}
