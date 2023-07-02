package es.serversurvival.pixelcoins.deudas.pagarcuotas;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class CuotaPagada extends PixelcoinsEvento {
    @Getter private final UUID deudaId;
    @Getter private final double cuota;
    @Getter private final UUID deudorJugadorId;
}
