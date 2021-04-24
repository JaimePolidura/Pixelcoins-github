package es.serversurvival.nfs.empleados.contratar;

import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.enums.TipoSueldo;

public final class ContratarUseCase implements AllMySQLTablesInstances {
    public static final ContratarUseCase INSTANCE = new ContratarUseCase();

    private ContratarUseCase () {}

    public void contratar (String nombreAContratar, String empresa, double salario, TipoSueldo tipoSueldo, String cargo) {
        empleadosMySQL.nuevoEmpleado(nombreAContratar, empresa, salario, tipoSueldo, cargo);
    }
}
