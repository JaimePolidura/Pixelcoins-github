package es.serversurvival.pixelcoins.deudas.emitir;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class DeudaEmitida extends PixelcoinsEvento {
    @Getter private final EmitirDeudaParametros deudaEmitidaParametros;
}
