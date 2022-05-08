package es.serversurvival.empresas.logitipo;

import es.jaime.javaddd.domain.exceptions.IllegalType;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas._shared.domain.Empresa;
import org.bukkit.Material;

public final class CambiarLogitpoUseCase implements AllMySQLTablesInstances {
    private final EmpresasService empresasService;

    public CambiarLogitpoUseCase() {
        this.empresasService = DependecyContainer.get(EmpresasService.class);
    }

    public void cambiar (String empresaNombre, Material logotipo, String playerName) {
        this.ensureCorrectFormatLogotipo(logotipo);
        var empresa = this.empresasService.getEmpresaByNombre(empresaNombre);
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
