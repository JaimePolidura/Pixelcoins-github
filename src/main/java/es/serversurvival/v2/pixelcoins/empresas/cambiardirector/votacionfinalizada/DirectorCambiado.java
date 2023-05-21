package es.serversurvival.v2.pixelcoins.empresas.cambiardirector.votacionfinalizada;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class DirectorCambiado extends PixelcoinsEvento {
    @Getter private final UUID empresaId;
    @Getter private final UUID antiguoDirectorJugadorId;
    @Getter private final UUID nuevoDirectorJugadorId;
}
