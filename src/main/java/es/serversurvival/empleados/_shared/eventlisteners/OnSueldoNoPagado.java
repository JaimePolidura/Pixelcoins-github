package es.serversurvival.empleados._shared.eventlisteners;

import es.serversurvival.shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class OnSueldoNoPagado extends PixelcoinsEvento {
    @Getter private final String empleado;
    @Getter private final String empresa;
}
