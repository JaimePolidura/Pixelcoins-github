package es.serversurvival.v2.pixelcoins.empresas.emitir;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class AccionesEmitidasEmpresa extends PixelcoinsEvento {
    @Getter private final EmitirAccionesServerParametros emitirAccionesServerParametros;
}
