package es.serversurvival.nfs.empleados.eventlisteners;

import es.serversurvival.nfs.shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class OnSueldoNoPagado extends PixelcoinsEvento {
    @Getter private final String empleado;
    @Getter private final String empresa;
}
