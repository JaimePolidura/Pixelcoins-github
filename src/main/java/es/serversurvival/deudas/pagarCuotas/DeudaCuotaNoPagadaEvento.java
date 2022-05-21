package es.serversurvival.deudas.pagarCuotas;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class DeudaCuotaNoPagadaEvento extends PixelcoinsEvento {
    @Getter private final String acredor;
    @Getter private final String deudor;
    @Getter private final UUID deudaId;

    public static DeudaCuotaNoPagadaEvento of (String acredor, String deudor, UUID deudaId) {
        return new DeudaCuotaNoPagadaEvento(acredor, deudor, deudaId);
    }
}
