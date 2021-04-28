package es.serversurvival.empleados.irse;

import es.serversurvival.Pixelcoin;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.empleados.mysql.Empleado;
import es.serversurvival.empresas.mysql.Empresa;

public final class IrseEmpresaUseCase implements AllMySQLTablesInstances {
    public static final IrseEmpresaUseCase INSTANCE = new IrseEmpresaUseCase();

    private IrseEmpresaUseCase() {}

    public void irse (String empleadoNombre, String empresaNombre) {
        Empleado empleado = empleadosMySQL.getEmpleado(empleadoNombre, empresaNombre);
        Empresa empresa = empresasMySQL.getEmpresa(empresaNombre);

        empleadosMySQL.borrarEmplado(empleado.getId());

        Pixelcoin.publish(new EmpleadoDejaEmpresaEvento(empleadoNombre, empresa));
    }
}
