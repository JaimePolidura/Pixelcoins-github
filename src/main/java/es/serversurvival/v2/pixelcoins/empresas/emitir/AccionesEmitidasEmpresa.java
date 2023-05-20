package es.serversurvival.v2.pixelcoins.empresas.emitir;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class AccionesEmitidasEmpresa extends PixelcoinsEvento {
    @Getter private final UUID ofertaId;
}
