package es.serversurvival.v2.pixelcoins.deudas.comprar;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.deudas._shared.OfertaDeudaMercadoPrimario;
import es.serversurvival.v2.pixelcoins.deudas.prestar.PrestarDeudaUseCase;
import es.serversurvival.v2.pixelcoins.deudas.prestar.PrestarDeudaParametros;
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
                PrestarDeudaParametros.fromOfertaDeudaMercadoPrimario(ofertaComprada, compradorId)
        );

        eventBus.publish(new DeudaComprada(deudaId, compradorId, ofertaComprada.getPrecio()));
    }
}
