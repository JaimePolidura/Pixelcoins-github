package es.serversurvival.v2.pixelcoins.empresas.comprar;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class AccionServerComprada extends PixelcoinsEvento {
    @Getter private final UUID empresaId;
    @Getter private final UUID jugadorId;
    @Getter private final double precioPorAccion;
}
