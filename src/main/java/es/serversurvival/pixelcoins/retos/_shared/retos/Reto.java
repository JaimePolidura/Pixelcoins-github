package es.serversurvival.pixelcoins.retos._shared.retos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class Reto {
    @Getter private final int retoId;
    @Getter private final String nombre;
    @Getter private final String descripccion;
    @Getter private final ModuloReto moduloReto;
    @Getter private final UUID retoAnteriorId;
    @Getter private final UUID retoPadreProgresionId;
    @Getter private final TipoReto tipo;
    @Getter private final double cantidadRequerida;
}
