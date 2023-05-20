package es.serversurvival.v2.pixelcoins.empresas.cerrar;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class CerrarEmpresaParametros {
    @Getter private final UUID empresaId;
    @Getter private final UUID jugadorId;
}
