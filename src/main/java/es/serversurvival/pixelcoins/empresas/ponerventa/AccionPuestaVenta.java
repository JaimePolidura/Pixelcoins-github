package es.serversurvival.pixelcoins.empresas.ponerventa;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.retos._shared.retos.RetoMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public final class AccionPuestaVenta extends PixelcoinsEvento {
    @Getter private final PonerVentaAccionesParametros ponerVentaAccionesParametros;
}
