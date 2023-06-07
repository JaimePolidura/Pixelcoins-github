package es.serversurvival.pixelcoins.mercado.retirar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.serversurvival.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.pixelcoins.mercado._shared.OfertasValidator;
import es.serversurvival.pixelcoins.mercado._shared.accion.OfertaAccionCaller;
import es.serversurvival.pixelcoins.mercado._shared.accion.OfertaRetiradaListener;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class RetirarOfertaUseCase {
    private final OfertaAccionCaller ofertaAccionCaller;
    private final OfertasValidator ofertasValidator;
    private final OfertasService ofertasService;

    public void retirarOfertaUseCase(RetirarOfertaParametros parametros) {
        ofertasValidator.esVendedor(parametros.getOfertaId(), parametros.getRetiradorId());

        Oferta ofertaRetirada = ofertasService.getById(parametros.getOfertaId());
        ofertasService.deleteById(ofertaRetirada.getOfertaId());

        ofertaAccionCaller.call(OfertaRetiradaListener.class, ofertaRetirada, parametros.getRetiradorId());
    }
}
