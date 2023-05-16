package es.serversurvival.v2.pixelcoins.deudas.pagarcuotas;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class CuotaDeudaPagadaEvento extends PixelcoinsEvento {
    @Getter private final UUID duedaId;
    @Getter private final double cuota;
}
