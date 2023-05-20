package es.serversurvival.v2.pixelcoins.empresas.editarempresa;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class EditarEmpresaParametros {
    @Getter private final UUID empresaId;
    @Getter private final UUID jugadorId;
    @Getter private final String nuevoNombre;
    @Getter private final String nuevaDescripccion;
    @Getter private final String nuevoIcono;
}
