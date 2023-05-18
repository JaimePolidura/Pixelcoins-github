package es.serversurvival.v2.pixelcoins.mercado.ofrecer;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.v2.pixelcoins.mercado._shared.Oferta;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class NuevaOferta extends PixelcoinsEvento {
    @Getter private final Oferta oferta;
}
