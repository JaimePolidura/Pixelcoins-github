package es.serversurvival.pixelcoins.deudas.ponerventa;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class DeudaPuestaVenta extends PixelcoinsEvento {
    @Getter private final PonerVentaDeudaParametros ponerVentaDeudaParametros;
}
