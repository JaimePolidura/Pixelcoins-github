package es.serversurvival.v2.pixelcoins.empresas.crear;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class CrearEmpresaParametros {
    @Getter private final UUID jugadorCreadorId;
    @Getter private final String nombre;
    @Getter private final String descripccion;
    @Getter private final String icono;
}
