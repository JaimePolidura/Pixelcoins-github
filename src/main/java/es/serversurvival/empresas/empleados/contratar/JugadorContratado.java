package es.serversurvival.empresas.empleados.contratar;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class JugadorContratado extends PixelcoinsEvento {
    @Getter private final String jugadorNombre;
    @Getter private final String empresa;
    @Getter private final String cargo;
}
