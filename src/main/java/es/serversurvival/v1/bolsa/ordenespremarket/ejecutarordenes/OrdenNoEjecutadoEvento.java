package es.serversurvival.v1.bolsa.ordenespremarket.ejecutarordenes;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public final class OrdenNoEjecutadoEvento extends PixelcoinsEvento {
    @Getter private final String jugadorNombre;
    @Getter private final String nombreActivo;
    @Getter private final int cantidad;

    public static OrdenNoEjecutadoEvento of(String jugadorNombre, String nombreActivo, int cantidad){
        return new OrdenNoEjecutadoEvento(jugadorNombre, nombreActivo, cantidad);
    }
}
