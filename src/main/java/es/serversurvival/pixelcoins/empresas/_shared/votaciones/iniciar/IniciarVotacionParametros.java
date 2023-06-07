package es.serversurvival.pixelcoins.empresas._shared.votaciones.iniciar;

import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.Votacion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class IniciarVotacionParametros {
    @Getter private final UUID jugadorId;
    @Getter private final UUID empresaId;
    @Getter private final Votacion votacion;
}
