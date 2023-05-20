package es.serversurvival.v2.pixelcoins.empresas.despedir;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class DespedirEmpleadoParametros {
    @Getter private final UUID empleadoIdADespedir;
    @Getter private final UUID jugadorIdQueDespide;
    @Getter private final UUID empresaId;
    @Getter private final String causa;
}
