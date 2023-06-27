package es.serversurvival.pixelcoins.deudas.prestar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.Validador;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.Transaccion;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasService;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasValidador;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class PrestarDeudaUseCase implements UseCaseHandler<PrestarDeudaParametros> {
    private final TransaccionesService transaccionesService;
    private final DeudasValidador deudasValidador;
    private final DeudasService deudasService;
    private final Validador validador;
    private final EventBus eventBus;

    @Override
    public void handle(PrestarDeudaParametros parametros) {
        validar(parametros);

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
    }
    
    public void validar(PrestarDeudaParametros parametros) {
        validador.notEqual(parametros.getAcredorJugadorId(), parametros.getDeudorJugadorId(), "No te puedes prestar a ti mismo");
        deudasValidador.nominalCorrecto(parametros.getNominal());
        deudasValidador.interesCorreto(parametros.getInteres());
        deudasValidador.numeroCuotasCorrecto(parametros.getNumeroCuotasTotales());
        deudasValidador.periodoPagoCuotasCorrecto(parametros.getPeriodoPagoCuota());
        validador.jugadorTienePixelcoins(parametros.getAcredorJugadorId(), parametros.getNominal());
    }
}
