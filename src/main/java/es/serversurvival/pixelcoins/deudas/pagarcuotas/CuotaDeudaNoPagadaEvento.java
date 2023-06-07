package es.serversurvival.pixelcoins.deudas.pagarcuotas;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class CuotaDeudaNoPagadaEvento extends PixelcoinsEvento {
    @Getter private final UUID deudaId;
    @Getter private final double cuota;
    @Getter private final UUID deudorJugadorId;
}
