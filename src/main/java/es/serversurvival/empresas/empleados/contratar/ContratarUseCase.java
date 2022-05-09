package es.serversurvival.empresas.empleados.contratar;

import es.jaime.javaddd.domain.exceptions.AlreadyExists;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.TipoSueldo;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;

public final class ContratarUseCase implements AllMySQLTablesInstances {
    private final EmpleadosService empleadosService;
    private final EmpresasService empresasService;

    public ContratarUseCase() {
        this.empleadosService = DependecyContainer.get(EmpleadosService.class);
        this.empresasService = DependecyContainer.get(EmpresasService.class);
    }

    public void contratar (String ownerEmpresa, String jugadorAContratar, String empresaNombre, double salario, TipoSueldo tipoSueldo, String cargo) {
        var empresa = this.ensureEmpresaExists(empresaNombre);
        this.ensureOwner(ownerEmpresa, empresa);
        this.ensureNotWorksForEmpresa(empresa, jugadorAContratar);

        this.empleadosService.save(jugadorAContratar, empresaNombre, salario, tipoSueldo, cargo);
    }

    private void correctFormatPixelcoins(){

    }

    private void ensureNotWorksForEmpresa(Empresa empresa, String jugadorAContratar){
        var alreadyWorks = this.empleadosService.findByEmpresa(empresa.getNombre()).stream()
                .anyMatch(empleoDeEmpresa -> empleoDeEmpresa.getNombre().equalsIgnoreCase(jugadorAContratar));

        if(alreadyWorks)
            throw new AlreadyExists("El jugador ya trabaja en tu empresa");
    }

    private Empresa ensureEmpresaExists(String empresaNombre) {
        return this.empresasService.getEmpresaByNombre(empresaNombre);
    }

    private void ensureOwner(String owner, Empresa empresa){
        if(empresa.getOwner().equalsIgnoreCase(owner))
            throw new NotTheOwner("Empresa incorrecta");
    }
}
