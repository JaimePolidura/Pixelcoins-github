package es.serversurvival.nfs.empleados.eventlisteners;

import es.serversurvival.legacy.mySQL.eventos.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class OnSueldoNoPagado extends PixelcoinsEvento {
    @Getter private final String empleado;
    @Getter private final String empresa;
}
