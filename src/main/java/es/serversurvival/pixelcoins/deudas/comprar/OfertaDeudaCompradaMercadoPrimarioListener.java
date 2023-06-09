package es.serversurvival.pixelcoins.deudas.comprar;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins.deudas.prestar.PrestarDeudaParametros;
import es.serversurvival.pixelcoins.deudas.prestar.PrestarDeudaUseCase;
import es.serversurvival.pixelcoins.mercado._shared.accion.OfertaCompradaListener;
import es.serversurvival.pixelcoins.deudas._shared.OfertaDeudaMercadoPrimario;
import lombok.AllArgsConstructor;

import java.util.UUID;

@EventHandler
@AllArgsConstructor
public final class OfertaDeudaCompradaMercadoPrimarioListener implements OfertaCompradaListener<OfertaDeudaMercadoPrimario> {
    private final PrestarDeudaUseCase prestarDeudaUseCase;

    @Override
    public void on(OfertaDeudaMercadoPrimario ofertaComprada, UUID compradorId) {
         prestarDeudaUseCase.handle(
                PrestarDeudaParametros.fromOfertaDeudaMercadoPrimario(ofertaComprada, compradorId)
        );
    }
}
