package es.serversurvival.v2.pixelcoins.deudas._shared;

import es.serversurvival.v2.pixelcoins.deudas.prestar.PrestarDeudaParametros;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class Deuda {
    @Getter private final UUID deudaId;
    @Getter private final UUID acredorJugadorId;
    @Getter private final UUID deudorJugadorId;
    @Getter private final double nominal;
    @Getter private final double interes;
    @Getter private final int nCuotasTotales;
    @Getter private final int nCuotasPagadas;
    @Getter private final int nCuotasImpagadas;
    @Getter private final LocalDateTime fechaUltimoPagoCuota;
    @Getter private final LocalDateTime fechaCreacion;
    @Getter private final long periodoPagoCuotaMs;
    @Getter private final EstadoDeuda estadoDeuda;

    public double getCuota() {
        return this.nCuotasPagadas + 1 == this.nCuotasImpagadas ?
                this.nominal :
                this.nominal * this.interes;
    }

    public double getPixelcoinsTodasLasCuotasRestantes() {
        double cuota = 0;

        for (int i = 0; i < nCuotasTotales - nCuotasPagadas - 1; i++)
            cuota += interes * nominal;
        cuota += cuota + nominal;

        return cuota;
    }

    public Deuda nuevoAcredor(UUID nuevoAcredorId) {
        return new Deuda(
                deudaId, nuevoAcredorId, deudorJugadorId, nominal, interes, nCuotasTotales, nCuotasTotales,
                nCuotasImpagadas, fechaUltimoPagoCuota, fechaCreacion, periodoPagoCuotaMs, estadoDeuda
        );
    }

    public Deuda cancelar() {
        return new Deuda(
                deudaId, acredorJugadorId, deudorJugadorId, nominal, interes, nCuotasTotales, nCuotasTotales,
                nCuotasImpagadas, fechaUltimoPagoCuota, fechaCreacion, periodoPagoCuotaMs, EstadoDeuda.CANCELADA
        );
    }

    public Deuda anotarPagadoPorCompleto() {
        return new Deuda(
                deudaId, acredorJugadorId, deudorJugadorId, nominal, interes, nCuotasTotales, nCuotasTotales,
                nCuotasImpagadas, LocalDateTime.now(), fechaCreacion, periodoPagoCuotaMs, EstadoDeuda.PAGADA
        );
    }

    public Deuda anotarPagoCuota() {
        return new Deuda(
                deudaId, acredorJugadorId, deudorJugadorId, nominal, interes, nCuotasTotales, nCuotasPagadas + 1,
                nCuotasImpagadas, fechaUltimoPagoCuota.plusNanos(periodoPagoCuotaMs * 1_000_000), fechaCreacion, periodoPagoCuotaMs, nCuotasImpagadas + 1 == nCuotasTotales ? EstadoDeuda.PAGADA : EstadoDeuda.PENDIENTE
        );
    }

    public Deuda incrementarNImpago() {
        return new Deuda(
                deudaId, acredorJugadorId, deudorJugadorId, nominal, interes, nCuotasTotales, nCuotasPagadas,
                nCuotasImpagadas + 1, fechaUltimoPagoCuota, fechaCreacion, periodoPagoCuotaMs, EstadoDeuda.PENDIENTE
        );
    }

    public boolean estaPendiente() {
        return this.estadoDeuda == EstadoDeuda.PENDIENTE;
    }

    public static Deuda fromParametrosUseCase(PrestarDeudaParametros parametros) {
        LocalDateTime fechaHoy = LocalDateTime.now();

        return new Deuda(
                UUID.randomUUID(),
                parametros.getAcredorJugadorId(),
                parametros.getDeudorJugadorId(),
                parametros.getNominal(),
                parametros.getInteres(),
                parametros.getNumeroCuotasTotales(),
                0,
                0,
                fechaHoy,
                fechaHoy,
                parametros.getPeriodoPagoCuita(),
                EstadoDeuda.PENDIENTE
        );
    }
}
