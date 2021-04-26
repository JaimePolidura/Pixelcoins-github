package es.serversurvival.nfs.empresas.tasks;

import es.serversurvival.legacy.mySQL.eventos.PixelcoinsEvento;
import es.serversurvival.nfs.empresas.mysql.Empresa;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class SueldoNoPagadoEmpresaEvento extends PixelcoinsEvento {
    @Getter private final Empresa empresa;
    @Getter private final Empleado empleado;
}
