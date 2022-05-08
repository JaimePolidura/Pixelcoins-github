package es.serversurvival.empresas.empleados.irse;

import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.empresas.empleados._shared.mysql.Empleado;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;

public final class IrseEmpresaUseCase implements AllMySQLTablesInstances {
    public static final IrseEmpresaUseCase INSTANCE = new IrseEmpresaUseCase();
    private final EmpresasService empresasService;

    public IrseEmpresaUseCase() {
        this.empresasService = DependecyContainer.get(EmpresasService.class);
    }

    public void irse (String empleadoNombre, String empresaNombre) {
        Empleado empleado = empleadosMySQL.getEmpleado(empleadoNombre, empresaNombre);
        Empresa empresa = empresasService.getEmpresaByNombre(empresaNombre);

        empleadosMySQL.borrarEmplado(empleado.getId());

        Pixelcoin.publish(new EmpleadoDejaEmpresaEvento(empleadoNombre, empresa));
    }
}
