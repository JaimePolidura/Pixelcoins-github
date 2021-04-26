package es.serversurvival.nfs.empleados.irse;

import es.serversurvival.legacy.mySQL.eventos.PixelcoinsEvento;
import es.serversurvival.nfs.empresas.mysql.Empresa;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class EmpleadoDejaEmpresaEvento extends PixelcoinsEvento {
    @Getter private final String empleado;
    @Getter private final Empresa empresa;
}
