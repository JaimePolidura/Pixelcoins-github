package es.serversurvival.pixelcoins.empresas.ponerventa;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class AccionPuestaVenta extends PixelcoinsEvento {
    @Getter private final PonerVentaAccionesParametros ponerVentaAccionesParametros;
}
