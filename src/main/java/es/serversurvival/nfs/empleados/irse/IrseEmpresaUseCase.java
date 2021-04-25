package es.serversurvival.nfs.empleados.irse;

import es.serversurvival.legacy.main.Pixelcoin;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.tablasObjetos.Empleado;
import es.serversurvival.legacy.mySQL.tablasObjetos.Empresa;

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
