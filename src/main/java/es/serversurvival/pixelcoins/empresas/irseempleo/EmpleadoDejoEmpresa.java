package es.serversurvival.pixelcoins.empresas.irseempleo;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class EmpleadoDejoEmpresa extends PixelcoinsEvento {
    @Getter private final UUID empresaId;
    @Getter private final UUID jugadorId;
}
