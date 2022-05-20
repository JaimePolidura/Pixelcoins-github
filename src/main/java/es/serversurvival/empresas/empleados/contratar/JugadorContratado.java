package es.serversurvival.empresas.empleados.contratar;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class JugadorContratado extends PixelcoinsEvento {
    @Getter private final String jugadorNombre;
    @Getter private final String empresa;
    @Getter private final String cargo;

    public static JugadorContratado of(String jugadorNombre, String empresa, String cargo){
        return new JugadorContratado(jugadorNombre, empresa, cargo);
    }
}
