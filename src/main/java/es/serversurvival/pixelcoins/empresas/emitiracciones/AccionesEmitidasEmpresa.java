package es.serversurvival.pixelcoins.empresas.emitiracciones;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class AccionesEmitidasEmpresa extends PixelcoinsEvento {
    @Getter private final UUID empresaId;
    @Getter private final int nAccionesAEmitir;
    @Getter private final double precioPorAccion;
}
