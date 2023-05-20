package es.serversurvival.v2.pixelcoins.mercado.ofrecer;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.serversurvival.v2.pixelcoins._shared.Validador;
import es.serversurvival.v2.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.v2.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.v2.pixelcoins.mercado._shared.OfertasValidator;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class OfrecerOfertaUseCase {
    private final OfertasValidator ofertasValidator;
    private final OfertasService ofertasService;
    private final Validador validador;

    public void ofrecer(Oferta nuevaOferta) {
        validador.numeroMayorQueCero(nuevaOferta.getPrecio(), "El precio");
        validador.numeroMayorQueCero(nuevaOferta.getCantidad(), "La cantidad");
        validador.noNull(nuevaOferta.getVendedorId(), "El vendedor");
        validador.noNull(nuevaOferta.getTipoOferta(), "El tipo oferta");
        ofertasValidator.ofertaNoEsRepetida(nuevaOferta.getTipoOferta(), nuevaOferta.getObjeto());

        ofertasService.save(nuevaOferta);
    }
}
