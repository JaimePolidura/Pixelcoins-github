package es.serversurvival.empresas.borrar;

import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas._shared.domain.Empresa;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class EmpresaBorrarUseCase implements AllMySQLTablesInstances {
    private final EmpresasService empresasService;

    public EmpresaBorrarUseCase() {
        this.empresasService = DependecyContainer.get(EmpresasService.class);
    }

    public void borrar (String owner, String empresaNombre) {
        Empresa empresaABorrar = this.empresasService.getEmpresaByNombre(empresaNombre);
        this.ensureOwner(empresaABorrar, owner);

        this.empresasService.deleteByEmpresaId(empresaABorrar.getEmpresaId());

        Pixelcoin.publish(new EmpresaBorradaEvento(owner, empresaNombre, empresaABorrar.getPixelcoins()));
    }

    private void ensureOwner(Empresa empresa, String ownerSupuesto){
        if(!empresa.getOwner().equals(ownerSupuesto))
            throw new NotTheOwner("No eres el owner de la empresa");
    }
}
