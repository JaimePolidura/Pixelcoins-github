package es.serversurvival.empleados.irse;

import es.serversurvival.shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.empresas.mysql.Empresa;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class EmpleadoDejaEmpresaEvento extends PixelcoinsEvento {
    @Getter private final String empleado;
    @Getter private final Empresa empresa;
}
