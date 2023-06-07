package es.serversurvival.pixelcoins.deudas.prestar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.Validador;
import es.serversurvival.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.Transaccion;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import es.serversurvival.pixelcoins.deudas._shared.Deuda;
import es.serversurvival.pixelcoins.deudas._shared.DeudasService;
import es.serversurvival.pixelcoins.deudas._shared.DeudasValidador;
import lombok.AllArgsConstructor;

import java.util.UUID;

@UseCase
@AllArgsConstructor
public final class PrestarDeudaUseCase {
    private final TransaccionesService transaccionesService;
    private final DeudasValidador deudasValidador;
    private final DeudasService deudasService;
    private final Validador validador;
    private final EventBus eventBus;

    public UUID prestar(PrestarDeudaParametros parametros) {
        deudasValidador.nominalCorrecto(parametros.getNominal());
        deudasValidador.interesCorreto(parametros.getInteres());
        deudasValidador.numeroCuotasCorrecto(parametros.getNumeroCuotasTotales());
        deudasValidador.periodoPagoCuotasCorrecto(parametros.getPeriodoPagoCuita());
        validador.jugadorTienePixelcoins(parametros.getAcredorJugadorId(), parametros.getNominal());

        Deuda deuda = Deuda.fromParametrosUseCase(parametros);

        deudasService.save(deuda);
        transaccionesService.save(Transaccion.builder()
                        .pagadorId(parametros.getAcredorJugadorId())
                        .pagadoId(parametros.getDeudorJugadorId())
                        .pixelcoins(parametros.getNominal())
                        .objeto(deuda.getDeudaId().toString())
                        .tipo(TipoTransaccion.DEUDAS_PRIMER_DESEMBOLSO)
                .build());

        eventBus.publish(new PixelcoinsPrestadasEvento(deuda));

        return deuda.getDeudaId();
    }
}
