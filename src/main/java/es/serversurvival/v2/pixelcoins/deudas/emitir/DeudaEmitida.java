package es.serversurvival.v2.pixelcoins.deudas.emitir;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class DeudaEmitida extends PixelcoinsEvento {
    @Getter private final EmitirDeudaUseCaseParametros deudaEmitidaParametros;
}
