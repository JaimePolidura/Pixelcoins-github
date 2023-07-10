package es.serversurvival.pixelcoins.mercado.retirar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.pixelcoins.mercado._shared.OfertasValidator;
import es.serversurvival.pixelcoins.mercado._shared.custom.OfertaCustomCaller;
import es.serversurvival.pixelcoins.mercado._shared.custom.accion.OfertaRetiradaListener;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class RetirarOfertaUseCase implements UseCaseHandler<RetirarOfertaParametros> {
    private final OfertaCustomCaller ofertaAccionCaller;
    private final OfertasValidator ofertasValidator;
    private final OfertasService ofertasService;

    @Override
    public void handle(RetirarOfertaParametros parametros) {
        ofertasValidator.esVendedor(parametros.getOfertaId(), parametros.getRetiradorId());

        Oferta ofertaRetirada = ofertasService.getById(parametros.getOfertaId());
        ofertasService.deleteById(ofertaRetirada.getOfertaId());

        ofertaAccionCaller.callAccion(OfertaRetiradaListener.class, ofertaRetirada, parametros.getRetiradorId());
    }
}
