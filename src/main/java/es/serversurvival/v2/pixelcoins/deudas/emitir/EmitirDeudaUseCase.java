package es.serversurvival.v2.pixelcoins.deudas.emitir;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.deudas._shared.DeudasValidador;
import es.serversurvival.v2.pixelcoins.deudas.comprar.primario.OfertaDeudaMercadoPrimario;
import es.serversurvival.v2.pixelcoins.mercado.ofrecer.OfrecerOfertaUseCase;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public final class EmitirDeudaUseCase {
    private final OfrecerOfertaUseCase ofrecerOfertaUseCase;
    private final DeudasValidador deudasValidador;
    private final EventBus eventBus;

    public void emitir(EmitirDeudaUseCaseParametros parametros) {
        deudasValidador.periodoPagoCuotasCorrecto(parametros.getPeriodoPagoCuota());
        deudasValidador.numeroCuotasCorrecto(parametros.getNumeroCuotasTotales());
        deudasValidador.nominalCorrecto(parametros.getNominal());
        deudasValidador.interesCorreto(parametros.getInteres());

        OfertaDeudaMercadoPrimario oferta = OfertaDeudaMercadoPrimario.fromParametosEmitirDeuda(parametros);

        ofrecerOfertaUseCase.ofrecer(oferta);

        eventBus.publish(new DeudaEmitida(oferta.getOfertaId()));
    }
}
