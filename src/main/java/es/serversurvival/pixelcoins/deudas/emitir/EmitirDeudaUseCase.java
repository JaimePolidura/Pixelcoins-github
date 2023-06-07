package es.serversurvival.pixelcoins.deudas.emitir;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins.deudas._shared.DeudasValidador;
import es.serversurvival.pixelcoins.deudas._shared.OfertaDeudaMercadoPrimario;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.pixelcoins.mercado.ofrecer.OfrecerOfertaUseCase;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public final class EmitirDeudaUseCase {
    private final OfrecerOfertaUseCase ofrecerOfertaUseCase;
    private final DeudasValidador deudasValidador;
    private final EventBus eventBus;

    public void emitir(EmitirDeudaParametros parametros) {
        deudasValidador.periodoPagoCuotasCorrecto(parametros.getPeriodoPagoCuota());
        deudasValidador.numeroCuotasCorrecto(parametros.getNumeroCuotasTotales());
        deudasValidador.nominalCorrecto(parametros.getNominal());
        deudasValidador.interesCorreto(parametros.getInteres());

        ofrecerOfertaUseCase.ofrecer(OfertaDeudaMercadoPrimario.builder()
                        .vendedorId(parametros.getJugadorId())
                        .precio(parametros.getNominal())
                        .tipoOferta(TipoOferta.DEUDA_MERCADO_PRIMARIO)
                        .interes(parametros.getInteres())
                        .numeroCuotasTotales(parametros.getNumeroCuotasTotales())
                        .periodoPagoCuota(parametros.getPeriodoPagoCuota())
                .build());

        eventBus.publish(new DeudaEmitida(parametros));
    }
}
