package es.serversurvival.empresas.empleados.despedir;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class EmpleadoDespedido extends PixelcoinsEvento {
    @Getter private final String empleadoNomrbe;
    @Getter private final String empresa;
    @Getter private final String razon;
}
