package es.serversurvival.empresas.empleados.eventlisteners;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class OnSueldoNoPagado extends PixelcoinsEvento {
    @Getter private final String empleado;
    @Getter private final String empresa;
}
