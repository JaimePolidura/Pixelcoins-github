package es.serversurvival.pixelcoins.mercado.ofrecer;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import es.serversurvival.pixelcoins.mercado._shared.Oferta;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class OfrecerOfertaParametros implements ParametrosUseCase {
    @Getter private final Oferta nuevaOferta;

    public static OfrecerOfertaParametros of(Oferta oferta) {
        return new OfrecerOfertaParametros(oferta);
    }
}
