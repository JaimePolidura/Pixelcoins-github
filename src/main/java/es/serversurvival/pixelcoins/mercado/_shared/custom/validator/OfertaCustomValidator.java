package es.serversurvival.pixelcoins.mercado._shared.custom.validator;

import es.serversurvival.pixelcoins.mercado._shared.Oferta;

import java.util.UUID;

public interface OfertaCustomValidator<T extends Oferta> {
    void validate(T oferta, UUID compradorId);
}
