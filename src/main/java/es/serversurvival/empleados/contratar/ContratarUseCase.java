package es.serversurvival.empleados.contratar;

import es.serversurvival.empleados.mysql.TipoSueldo;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class ContratarUseCase implements AllMySQLTablesInstances {
    public static final ContratarUseCase INSTANCE = new ContratarUseCase();

    private ContratarUseCase () {}

    public void contratar (String nombreAContratar, String empresa, double salario, TipoSueldo tipoSueldo, String cargo) {
        empleadosMySQL.nuevoEmpleado(nombreAContratar, empresa, salario, tipoSueldo, cargo);
    }
}
