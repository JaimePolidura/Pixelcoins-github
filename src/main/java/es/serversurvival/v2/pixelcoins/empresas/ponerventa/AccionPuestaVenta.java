package es.serversurvival.v2.pixelcoins.empresas.ponerventa;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class AccionPuestaVenta extends PixelcoinsEvento {
    @Getter private final PonerVentaAccionesParametros ponerVentaAccionesParametros;
}
