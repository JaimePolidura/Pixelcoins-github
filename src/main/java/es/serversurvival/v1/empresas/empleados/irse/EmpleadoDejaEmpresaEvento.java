package es.serversurvival.v1.empresas.empleados.irse;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class EmpleadoDejaEmpresaEvento extends PixelcoinsEvento {
    @Getter private final String empleado;
    @Getter private final String empresaNombre;
    @Getter private final String empresaOwner;

    public static EmpleadoDejaEmpresaEvento of (String empleado, String empresaNombre, String empresaOwner){
        return new EmpleadoDejaEmpresaEvento(empleado, empresaNombre, empresaOwner);
    }

}
