package es.serversurvival.empresas.editardescripccion;

import es.jaime.javaddd.domain.exceptions.IllegalLength;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas._shared.domain.Empresa;

public final class EditarDescUseCase {
    private final EmpresasService empresasService;

    public EditarDescUseCase() {
        this.empresasService = DependecyContainer.get(EmpresasService.class);
    }

    public void edit (String nombreEmpresa, String newDescipcion, String playerName) {
        this.ensureCorrectFormatDesc(newDescipcion);
        var empresaToChangeDesc = this.ensureEmpresaExists(nombreEmpresa);
        this.ensureOwner(playerName, empresaToChangeDesc);

        this.empresasService.save(empresaToChangeDesc.withDescripccion(newDescipcion));
    }

    private Empresa ensureEmpresaExists(String nombreEmpresa){
        return this.empresasService.getEmpresaByNombre(nombreEmpresa);
    }

    private void ensureOwner(String owner, Empresa empresa){
        if(!empresa.getOwner().equalsIgnoreCase(owner))
            throw new NotTheOwner("No eres el owner de la empresa");
    }

    private void ensureCorrectFormatDesc(String desc){
        if(desc == null || desc.length() <= 0 || desc.length() > EmpresasService.MAX_DESC_LONGITUD)
            throw new IllegalLength("La descripccion tiene que comprender entre 1 y 200 caracteres");
    }
}
