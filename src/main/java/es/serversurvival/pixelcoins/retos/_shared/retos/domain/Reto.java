package es.serversurvival.pixelcoins.retos._shared.retos.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reto {
    @Getter private int retoId;
    @Getter private String nombre;
    @Getter private String descripccion;
    @Getter private ModuloReto moduloReto;
    @Getter private UUID retoAnteriorId;
    @Getter private UUID retoPadreProgresionId;
    @Getter private TipoReto tipo;
    @Getter private double cantidadRequerida;
}
