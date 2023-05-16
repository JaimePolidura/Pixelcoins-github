package es.serversurvival.v2.pixelcoins.deudas.prestar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class PrestDeudaUseCaseParametros {
    @Getter private final UUID acredorJugadorId;
    @Getter private final UUID deudorJugadorId;
    @Getter private final double nominal;
    @Getter private final double interes;
    @Getter private final int numeroCuotasTotales;
    @Getter private final long periodoPagoCuita;
}
