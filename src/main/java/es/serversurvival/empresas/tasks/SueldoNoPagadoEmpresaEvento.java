package es.serversurvival.empresas.tasks;

import es.serversurvival.empresas.mysql.Empresa;
import es.serversurvival.shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class SueldoNoPagadoEmpresaEvento extends PixelcoinsEvento {
    @Getter private final Empresa empresa;
    @Getter private final Empleado empleado;
}
