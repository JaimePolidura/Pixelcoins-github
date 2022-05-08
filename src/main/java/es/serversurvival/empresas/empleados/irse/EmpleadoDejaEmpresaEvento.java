package es.serversurvival.empresas.empleados.irse;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class EmpleadoDejaEmpresaEvento extends PixelcoinsEvento {
    @Getter private final String empleado;
    @Getter private final Empresa empresa;
}
