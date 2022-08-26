package es.serversurvival.empresas.empresas.logitipo;

import es.jaime.javaddd.domain.exceptions.IllegalType;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaimetruman.annotations.UseCase;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import lombok.AllArgsConstructor;
import org.bukkit.Material;

@AllArgsConstructor
@UseCase
public final class EditarLogitpoUseCase {
    private final EmpresasService empresasService;

    public EditarLogitpoUseCase() {
        this.empresasService = DependecyContainer.get(EmpresasService.class);
    }

    public void cambiar (String empresaNombre, Material logotipo, String playerName) {
        this.ensureCorrectFormatLogotipo(logotipo);
        var empresa = this.empresasService.getByNombre(empresaNombre);
        this.ensureOwner(playerName, empresa);

        this.empresasService.save(empresa.withIcono(logotipo));
    }

    private void ensureOwner(String owner, Empresa empresa){
        if(!empresa.getOwner().equalsIgnoreCase(owner))
            throw new NotTheOwner("No eres el owner de la empresa");
    }

    private void ensureCorrectFormatLogotipo(Material material){
        if(material == null || material.isAir())
            throw new IllegalType("El logotipo no puede ser aire. Selecciona un item con la mano y ejecuta el comando");
    }
}
