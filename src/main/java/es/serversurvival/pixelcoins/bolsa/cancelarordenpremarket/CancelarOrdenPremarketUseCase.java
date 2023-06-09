package es.serversurvival.pixelcoins.bolsa.cancelarordenpremarket;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.bolsa._shared.BolsaValidator;
import es.serversurvival.pixelcoins.bolsa._shared.premarket.application.OrdenesPremarketService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public final class CancelarOrdenPremarketUseCase implements UseCaseHandler<CancelarOrdenPremarketParametros> {
    private final OrdenesPremarketService ordenesPremarketService;
    private final BolsaValidator validator;
    private final EventBus eventBus;

    @Override
    public void handle(CancelarOrdenPremarketParametros parametros) {
        validator.jugadorTieneOrden(parametros.getOrdenPremarketId(), parametros.getJugadorId());

        ordenesPremarketService.deleteById(parametros.getOrdenPremarketId());

        eventBus.publish(new OrdenPremarketCancelada(parametros.getJugadorId()));
    }
}
