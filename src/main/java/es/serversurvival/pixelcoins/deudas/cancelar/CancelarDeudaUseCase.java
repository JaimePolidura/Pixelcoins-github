package es.serversurvival.pixelcoins.deudas.cancelar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasService;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasValidador;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class CancelarDeudaUseCase implements UseCaseHandler<CancelarDeudaParametros> {
    private final DeudasValidador deudasValidador;
    private final OfertasService ofertasService;
    private final DeudasService deudasService;
    private final EventBus eventBus;

    @Override
    public void handle(CancelarDeudaParametros parametros) {
        deudasValidador.acredorDeDeuda(parametros.getDeudaId(), parametros.getJugadorId());
        deudasValidador.deudaPendiente(parametros.getDeudaId());

        Deuda deuda = deudasService.getById(parametros.getDeudaId());

        deudasService.save(deuda.cancelar());
        ofertasService.deleteByObjetoYTipo(deuda.getDeudaId(), TipoOferta.DEUDA_MERCADO_SECUNDARIO);

        eventBus.publish(new DeudaCancelada(deuda));
    }
}
