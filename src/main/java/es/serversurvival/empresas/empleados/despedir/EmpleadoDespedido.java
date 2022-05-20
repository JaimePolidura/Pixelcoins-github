package es.serversurvival.empresas.empleados.despedir;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class EmpleadoDespedido extends PixelcoinsEvento {
    @Getter private final String empleadoNombre;
    @Getter private final String empresa;
    @Getter private final String razon;

    public static EmpleadoDespedido of(String empleadoNomrbe, String empresa, String razon){
        return new EmpleadoDespedido(empleadoNomrbe, empresa, razon);
    }
}
