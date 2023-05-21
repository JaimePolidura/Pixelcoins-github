package es.serversurvival.v2.pixelcoins.deudas.emitir;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class EmitirDeudaParametros {
    @Getter private final UUID jugadorId;
    @Getter private final double nominal;
    @Getter private final double interes;
    @Getter private final int numeroCuotasTotales;
    @Getter private final long periodoPagoCuota;
}
