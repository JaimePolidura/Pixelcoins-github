package es.serversurvival.v2.pixelcoins.mercado._shared.accion;

import es.serversurvival.v2.pixelcoins.mercado._shared.Oferta;

import java.util.UUID;

public interface OfertaAccionListener<T extends Oferta> {
    void on(T ofertaComprada, UUID compradorId);
}
