package es.serversurvival.pixelcoins.deudas._shared.domain;

import es.serversurvival.pixelcoins.deudas.prestar.PrestarDeudaParametros;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class Deuda {
    @Getter private UUID deudaId;
    @Getter private UUID acredorJugadorId;
    @Getter private UUID deudorJugadorId;
    @Getter private double nominal;
    @Getter private double interes;
    @Getter private int nCuotasTotales;
    @Getter private int nCuotasPagadas;
    @Getter private int nCuotasImpagadas;
    @Getter private LocalDateTime fechaUltimoPagoCuota;
    @Getter private LocalDateTime fechaCreacion;
    @Getter private long periodoPagoCuotaMs;
    @Getter private EstadoDeuda estadoDeuda;

    public double getCuota() {
        return soloQuedaUnaCuotaPorPagar() ?
                nominal + nominal * interes :
                nominal;
    }

    public boolean soloQuedaUnaCuotaPorPagar() {
        return this.nCuotasPagadas + 1 == this.nCuotasTotales;
    }

    public double getPixelcoinsRestantesDePagar() {
        double cuota = interes * nominal;
        return getCuotasRestantes() * cuota + nominal;
    }

    public int getCuotasRestantes() {
        return nCuotasTotales - nCuotasPagadas;
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
