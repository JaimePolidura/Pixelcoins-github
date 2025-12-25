package es.serversurvival.pixelcoins.deudas.pagartodo;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.Validador;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.pixelcoins.transacciones.domain.TipoTransaccion;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasService;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasValidador;
import es.serversurvival.pixelcoins.transacciones.domain.Transaccion;
import es.serversurvival.pixelcoins.transacciones.application.TransaccionesSaver;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class PagarTodaLaDeudaUseCase implements UseCaseHandler<PagarTodaLaDeudaParametros> {
    private final TransaccionesSaver transaccionesSaver;
    private final DeudasValidador deudasValidador;
    private final OfertasService ofertasService;
    private final DeudasService deudasService;
    private final Validador validador;
    private final EventBus eventBus;

    @Override
    public void handle(PagarTodaLaDeudaParametros parametros) {
        Deuda deuda = this.deudasService.getById(parametros.getDeudaId());
        double pixelcoinsRestantes = deuda.getPixelcoinsRestantesDePagar();

        deudasValidador.deudorDeDeuda(parametros.getDeudaId(), parametros.getJugadorId());
        deudasValidador.deudaPendiente(parametros.getDeudaId());
        validador.jugadorTienePixelcoins(parametros.getJugadorId(), deuda.getPixelcoinsRestantesDePagar());

        deudasService.save(deuda.anotarPagadoPorCompleto());
        ofertasService.deleteByObjetoYTipo(parametros.getDeudaId(), TipoOferta.DEUDA_MERCADO_SECUNDARIO);
        transaccionesSaver.save(Transaccion.builder()
                .pagadoId(deuda.getAcredorJugadorId())
                .pagadorId(parametros.getJugadorId())
                .pixelcoins(pixelcoinsRestantes)
                .objeto(deuda.getDeudaId().toString())
                .tipo(TipoTransaccion.DEUDAS_PAGO_COMPLETO)
                .build());

        eventBus.publish(new DeudaPagadoPorCompleto(parametros.getDeudaId(), deuda.getDeudorJugadorId(), pixelcoinsRestantes));
    }
}
