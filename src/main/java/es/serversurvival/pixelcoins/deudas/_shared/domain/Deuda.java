package es.serversurvival.pixelcoins.deudas._shared.domain;

import es.serversurvival.pixelcoins.deudas.prestar.PrestarDeudaParametros;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
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
        return nominal * interes + (soloQuedaUnaCuotaPorPagar() ? nominal : 0);
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
        this.acredorJugadorId = nuevoAcredorId;
        return this;
    }

    public Deuda cancelar() {
        this.estadoDeuda = EstadoDeuda.CANCELADA;
        return this;
    }

    public Deuda anotarPagadoPorCompleto() {
        this.fechaUltimoPagoCuota = LocalDateTime.now();
        this.estadoDeuda = EstadoDeuda.PAGADA;

        return this;
    }

    public Deuda anotarPagoCuota() {
        this.nCuotasPagadas = this.nCuotasPagadas + 1;
        this.fechaUltimoPagoCuota = fechaUltimoPagoCuota.plusNanos(periodoPagoCuotaMs * 1_000_000);
        this.estadoDeuda = nCuotasPagadas == nCuotasTotales ? EstadoDeuda.PAGADA : EstadoDeuda.PENDIENTE;

        return this;
    }

    public Deuda incrementarNImpago() {
        this.nCuotasImpagadas = this.nCuotasImpagadas + 1;
        this.estadoDeuda = EstadoDeuda.PENDIENTE;

        return this;
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
                parametros.getPeriodoPagoCuota(),
                EstadoDeuda.PENDIENTE
        );
    }
}
