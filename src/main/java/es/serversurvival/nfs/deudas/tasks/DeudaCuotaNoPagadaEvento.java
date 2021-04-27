package es.serversurvival.nfs.deudas.tasks;

import es.serversurvival.nfs.shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class DeudaCuotaNoPagadaEvento extends PixelcoinsEvento {
    @Getter private final String acredor;
    @Getter private final String deudor;
    @Getter private final int deudaId;
}
