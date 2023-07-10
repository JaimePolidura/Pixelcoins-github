package es.serversurvival.pixelcoins.deudas.emitir;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasValidador;
import es.serversurvival.pixelcoins.deudas._shared.OfertaDeudaMercadoPrimario;
import es.serversurvival.pixelcoins.mercado.ponerventa.PonerVentaOfertaParametros;
import es.serversurvival.pixelcoins.mercado.ponerventa.PonerVentaOfertaUseCase;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public final class EmitirDeudaUseCase implements UseCaseHandler<EmitirDeudaParametros> {
    private final PonerVentaOfertaUseCase ofrecerOfertaUseCase;
    private final DeudasValidador deudasValidador;
    private final EventBus eventBus;

    @Override
    public void handle(EmitirDeudaParametros parametros) {
        deudasValidador.periodoPagoCuotasCorrecto(parametros.getPeriodoPagoCuota());
        deudasValidador.numeroCuotasCorrecto(parametros.getNumeroCuotasTotales());
        deudasValidador.nominalCorrecto(parametros.getNominal());
        deudasValidador.interesCorreto(parametros.getInteres());

        ofrecerOfertaUseCase.handle(PonerVentaOfertaParametros.of(OfertaDeudaMercadoPrimario.builder()
                .vendedorId(parametros.getJugadorId())
                .precio(parametros.getNominal())
                .interes(parametros.getInteres())
                .numeroCuotasTotales(parametros.getNumeroCuotasTotales())
                .periodoPagoCuota(parametros.getPeriodoPagoCuota())
                .build()));

        eventBus.publish(new DeudaEmitida(parametros));
    }
}
