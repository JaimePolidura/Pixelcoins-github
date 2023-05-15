package es.serversurvival.v1.empresas.empresas.pagarsueldostask;

import es.serversurvival.v1.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.v1.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class SueldoNoPagadoEmpresaEvento extends PixelcoinsEvento {
    @Getter private final Empresa empresa;
    @Getter private final Empleado empleado;
}
