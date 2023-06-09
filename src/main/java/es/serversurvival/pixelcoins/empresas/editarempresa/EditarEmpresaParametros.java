package es.serversurvival.pixelcoins.empresas.editarempresa;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class EditarEmpresaParametros implements ParametrosUseCase {
    @Getter private final UUID empresaId;
    @Getter private final UUID jugadorId;
    @Getter private final String nuevoNombre;
    @Getter private final String nuevaDescripccion;
    @Getter private final String nuevoIcono;
}
