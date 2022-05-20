package es.serversurvival.empresas.empresas.editarnombre;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class EmpresaNombreEditadoEvento extends PixelcoinsEvento {
    @Getter private final String antiguoNombre;
    @Getter private final String nuevoNombre;

    public static EmpresaNombreEditadoEvento of(String antiguoNombre, String nuevoNombre){
        return new EmpresaNombreEditadoEvento(antiguoNombre, nuevoNombre);
    }
}
