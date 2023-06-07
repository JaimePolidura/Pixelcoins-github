package es.serversurvival.pixelcoins.empresas.despedir;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class DespedirEmpleadoParametros {
    @Getter private final UUID empleadoIdADespedir;
    @Getter private final UUID jugadorId;
    @Getter private final UUID empresaId;
    @Getter private final String causa;
}
