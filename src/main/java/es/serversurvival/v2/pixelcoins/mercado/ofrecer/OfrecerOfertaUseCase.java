package es.serversurvival.v2.pixelcoins.mercado.ofrecer;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.v2.pixelcoins.mercado._shared.OfertasService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class OfrecerOfertaUseCase {
    private final OfertasService ofertasService;
    private final EventBus eventBus;

    public void vender(Oferta nuevaOferta) {
        ofertasService.save(nuevaOferta);

        eventBus.publish(new NuevaOferta(nuevaOferta));
    }
}
