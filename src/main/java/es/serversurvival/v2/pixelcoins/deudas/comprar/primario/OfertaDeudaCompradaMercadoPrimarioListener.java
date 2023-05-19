package es.serversurvival.v2.pixelcoins.deudas.comprar.primario;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.deudas.comprar.DeudaComprada;
import es.serversurvival.v2.pixelcoins.deudas.prestar.PrestarDeudaUseCase;
import es.serversurvival.v2.pixelcoins.deudas.prestar.PrestarDeudaUseCaseParametros;
import es.serversurvival.v2.pixelcoins.mercado._shared.accion.CustomOfertaCompradaListener;
import lombok.AllArgsConstructor;

import java.util.UUID;

@EventHandler
@AllArgsConstructor
public final class OfertaDeudaCompradaMercadoPrimarioListener implements CustomOfertaCompradaListener<OfertaDeudaMercadoPrimario> {
    private final PrestarDeudaUseCase prestarDeudaUseCase;
    private final EventBus eventBus;

    @Override
    public void on(OfertaDeudaMercadoPrimario ofertaComprada, UUID compradorId) {
        var deudaId = prestarDeudaUseCase.prestar(
                PrestarDeudaUseCaseParametros.fromOfertaDeudaMercadoPrimario(ofertaComprada, compradorId)
        );

        eventBus.publish(new DeudaComprada(deudaId, compradorId, ofertaComprada.getPrecio()));
    }
}
