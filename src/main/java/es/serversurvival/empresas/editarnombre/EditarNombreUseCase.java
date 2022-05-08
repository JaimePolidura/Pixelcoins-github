package es.serversurvival.empresas.editarnombre;

import es.jaime.javaddd.domain.exceptions.AlreadyExists;
import es.jaime.javaddd.domain.exceptions.IllegalLength;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas._shared.domain.Empresa;
import io.vavr.control.Try;

import static es.serversurvival.empresas._shared.application.EmpresasService.MAX_NOMBRE_LONGITUD;

public final class EditarNombreUseCase implements AllMySQLTablesInstances {
    private final EmpresasService empresasService;

    public EditarNombreUseCase(){
        this.empresasService = DependecyContainer.get(EmpresasService.class);
    }

    public void editar (String antiguoNombre, String nuevoNombre, String playerName) {
        this.ensureNombreEmpresaCorrectFormat(nuevoNombre);
        this.ensureNewNameNotTaken(nuevoNombre);
        var empresa = this.empresasService.getEmpresaByNombre(antiguoNombre);
        this.ensureOwnerOfEmpresa(empresa, playerName);

        empresasService.save(empresa.withNombre(nuevoNombre));

        Pixelcoin.publish(new EmpresaNombreEditadoEvento(antiguoNombre, nuevoNombre));
    }

    private void ensureOwnerOfEmpresa(Empresa empresa, String playerName){
        if(!empresa.getOwner().equalsIgnoreCase(playerName))
            throw new NotTheOwner("No eres el owner de la empresa");
    }

    private void ensureNombreEmpresaCorrectFormat(String nombreEmpresa){
        if(nombreEmpresa == null || nombreEmpresa.length() <= 0 || nombreEmpresa.length() > MAX_NOMBRE_LONGITUD)
            throw new IllegalLength("El nombre tiene que comprender entre 1 y 16 caracteres");
    }

    private void ensureNewNameNotTaken(String nombre){
        var taken = (Try.of(() -> this.empresasService.getEmpresaByNombre(nombre))).isFailure();

        if(!taken)
            throw new AlreadyExists("El nombre ya esta cogido");
    }
}
