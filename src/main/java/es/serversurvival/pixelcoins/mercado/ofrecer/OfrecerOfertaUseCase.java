package es.serversurvival.pixelcoins.mercado.ofrecer;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.serversurvival.pixelcoins._shared.Validador;
import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.pixelcoins.mercado._shared.OfertasValidator;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class OfrecerOfertaUseCase implements UseCaseHandler<OfrecerOfertaParametros> {
    private final OfertasValidator ofertasValidator;
    private final OfertasService ofertasService;
    private final Validador validador;

    @Override
    public void handle(OfrecerOfertaParametros parametros) throws Exception {
        Oferta nuevaOferta = parametros.getNuevaOferta();
        validador.numeroMayorQueCero(nuevaOferta.getPrecio(), "El precio");
        validador.numeroMayorQueCero(nuevaOferta.getCantidad(), "La cantidad");
        validador.noNull(nuevaOferta.getVendedorId(), "El vendedor");
        validador.noNull(nuevaOferta.getTipoOferta(), "El tipo oferta");
        ofertasValidator.ofertaNoEsRepetida(nuevaOferta.getTipoOferta(), nuevaOferta.getObjeto());

        ofertasService.save(nuevaOferta);
    }
}
