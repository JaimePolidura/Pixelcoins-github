package es.serversurvival.pixelcoins.mercado.ponerventa;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.mercado._shared.Oferta;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class OfertaPuestaEnVenta extends PixelcoinsEvento {
    @Getter private final Oferta oferta;
}
