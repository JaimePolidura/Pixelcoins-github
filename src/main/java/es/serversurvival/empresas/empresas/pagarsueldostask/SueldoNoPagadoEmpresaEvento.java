package es.serversurvival.empresas.empresas.pagarsueldostask;

import es.serversurvival.empresas.empleados._shared.mysql.Empleado;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class SueldoNoPagadoEmpresaEvento extends PixelcoinsEvento {
    @Getter private final Empresa empresa;
    @Getter private final Empleado empleado;
}
