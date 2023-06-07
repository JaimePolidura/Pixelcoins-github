package es.serversurvival.pixelcoins.empresas.dejarempleo;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class JugadorDejoEmpleo extends PixelcoinsEvento {
    @Getter private final UUID jugadorId;
    @Getter private final UUID empresaId;
}
