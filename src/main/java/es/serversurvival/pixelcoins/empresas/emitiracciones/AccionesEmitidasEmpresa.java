package es.serversurvival.pixelcoins.empresas.emitiracciones;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class AccionesEmitidasEmpresa extends PixelcoinsEvento {
    @Getter private final EmitirAccionesServerParametros emitirAccionesServerParametros;
}
