package es.serversurvival.v2.pixelcoins.mercado.retirar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.serversurvival.v2.pixelcoins.mercado._shared.*;
import es.serversurvival.v2.pixelcoins.mercado._shared.accion.CustomOfertaAccionCaller;
import es.serversurvival.v2.pixelcoins.mercado._shared.accion.CustomOfertaRetiradaListener;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class RetirarOfertaUseCase {
    private final CustomOfertaAccionCaller customOfertaAccionCaller;
    private final OfertasValidator ofertasValidator;
    private final OfertasService ofertasService;

    public void retirarOfertaUseCase(RetirarOfertaParametros parametros) {
        ofertasValidator.esVendedor(parametros.getOfertaId(), parametros.getRetiradorId());

        Oferta ofertaRetirada = ofertasService.getById(parametros.getOfertaId());
        ofertasService.deleteById(ofertaRetirada.getOfertaId());

        customOfertaAccionCaller.call(CustomOfertaRetiradaListener.class, ofertaRetirada, parametros.getRetiradorId());
    }
}
