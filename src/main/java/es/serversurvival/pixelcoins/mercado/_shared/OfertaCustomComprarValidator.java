package es.serversurvival.pixelcoins.mercado._shared;

import java.util.UUID;

public interface OfertaCustomComprarValidator<T extends Oferta> {
    void validate(T oferta, UUID compradorId);
}
