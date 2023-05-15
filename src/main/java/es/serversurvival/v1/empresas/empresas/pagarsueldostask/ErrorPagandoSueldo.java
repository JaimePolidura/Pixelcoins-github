package es.serversurvival.v1.empresas.empresas.pagarsueldostask;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class ErrorPagandoSueldo extends PixelcoinsEvento {
    @Getter private final String empleado;
    @Getter private final String empresa;
    @Getter private final String razon;

    public static ErrorPagandoSueldo of(String empleado, String empresa, String razon){
        return new ErrorPagandoSueldo(empleado, empresa, razon);
    }
}

