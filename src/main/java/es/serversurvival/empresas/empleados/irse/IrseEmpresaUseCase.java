package es.serversurvival.empresas.empleados.irse;

import es.jaime.javaddd.domain.exceptions.CannotBeYourself;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;

public final class IrseEmpresaUseCase {
    private final EmpresasService empresasService;
    private final EmpleadosService empleadosService;

    public IrseEmpresaUseCase() {
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.empleadosService = DependecyContainer.get(EmpleadosService.class);
    }

    public void irse (String empleadoNombre, String empresaNombre) {
        Empresa empresa = this.empresasService.getEmpresaByNombre(empresaNombre);
        this.ensureOwnerNotLeavingEmpresa(empresaNombre, empresa);
        Empleado empleado = this.empleadosService.getEmpleadoInEmpresa(empleadoNombre, empresaNombre);

        this.empleadosService.deleteById(empleado.getEmpleadoId());

        Pixelcoin.publish(new EmpleadoDejaEmpresaEvento(empleadoNombre, empresa));
    }

    private void ensureOwnerNotLeavingEmpresa(String owner, Empresa empresa){
        if(empresa.getOwner().equalsIgnoreCase(owner))
            throw new CannotBeYourself("No te puedes ir de tu propia empresa, si quieres irte /empresas help");
    }
}
