package es.serversurvival.pixelcoins.mercado._shared.custom.accion;

import es.serversurvival.pixelcoins.mercado._shared.Oferta;

import java.util.UUID;

public interface OfertaCustomAccionListener<T extends Oferta> {
    void on(T ofertaComprada, UUID compradorId);
}
