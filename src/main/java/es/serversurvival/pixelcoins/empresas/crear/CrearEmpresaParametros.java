package es.serversurvival.pixelcoins.empresas.crear;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class CrearEmpresaParametros implements ParametrosUseCase {
    @Getter private final UUID jugadorCreadorId;
    @Getter private final String nombre;
    @Getter private final String descripccion;
    @Getter private final String icono;
}
