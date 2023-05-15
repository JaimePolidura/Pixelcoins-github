package es.serversurvival.v1.empresas.empleados.irse;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.CannotBeYourself;
import es.serversurvival.v1.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.v1.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.v1.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.v1.empresas.empresas._shared.domain.Empresa;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@UseCase
public final class IrseEmpresaUseCase {
    private final EmpresasService empresasService;
    private final EmpleadosService empleadosService;
    private final EventBus eventBus;

    public void irse (String empleadoNombre, String empresaNombre) {
        Empresa empresa = this.empresasService.getByNombre(empresaNombre);
        this.ensureOwnerNotLeavingEmpresa(empleadoNombre, empresa);
        Empleado empleado = this.empleadosService.getEmpleadoInEmpresa(empleadoNombre, empresaNombre);

        this.empleadosService.deleteById(empleado.getEmpleadoId());

        this.eventBus.publish(new EmpleadoDejaEmpresaEvento(empleadoNombre, empresa.getNombre(), empresa.getOwner()));
    }

    private void ensureOwnerNotLeavingEmpresa(String owner, Empresa empresa){
        if(empresa.getOwner().equalsIgnoreCase(owner))
            throw new CannotBeYourself("No te puedes ir de tu propia empresa, si quieres irte /empresas help");
    }
}
