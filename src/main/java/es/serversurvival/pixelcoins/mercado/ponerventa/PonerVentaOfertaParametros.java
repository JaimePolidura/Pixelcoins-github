package es.serversurvival.pixelcoins.mercado.ponerventa;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import es.serversurvival.pixelcoins.mercado._shared.Oferta;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class PonerVentaOfertaParametros implements ParametrosUseCase {
    @Getter private final Oferta nuevaOferta;

    public static PonerVentaOfertaParametros of(Oferta oferta) {
        return new PonerVentaOfertaParametros(oferta);
    }
}
