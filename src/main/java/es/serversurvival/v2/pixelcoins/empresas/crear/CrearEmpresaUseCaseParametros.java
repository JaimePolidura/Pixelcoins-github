package es.serversurvival.v2.pixelcoins.empresas.crear;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class CrearEmpresaUseCaseParametros {
    @Getter private final UUID jugadorCreadorId;
    @Getter private final String nombre;
    @Getter private final String descripccion;
    @Getter private final String icono;
}
