package es.serversurvival.v2.ade.deudas;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public final class Deuda {
    private final UUID deudaId;
    private final UUID acredorJugadorId;
    private final UUID deudorJugadorId;
    private final double nominal;
    private final double precioUltimaTransaccion;
    private final double interes;
    private final int cuotasTotales;
    private final int cuotasPagadas;
    private final int cuotasImpagadas;
    private final LocalDateTime fechaUltimoPagoCuota;
    private final long periodoPagoCuota;
}
