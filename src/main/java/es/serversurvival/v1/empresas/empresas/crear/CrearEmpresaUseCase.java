package es.serversurvival.v1.empresas.empresas.crear;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.AlreadyExists;
import es.jaime.javaddd.domain.exceptions.IllegalLength;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.serversurvival.v1.empresas.empresas._shared.application.EmpresasService;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@UseCase
public final class CrearEmpresaUseCase {
    private final EmpresasService empresasService;
    private final EventBus eventBus;

    public void crear (String jugador, String nombreEmpresa, String descripcion) {
        this.ensureNombreEmpresaCorrectFormat(nombreEmpresa);
        this.ensureDescripccionCorrectFormat(descripcion);
        this.ensureUserNotPassMaxEmpresas(jugador);
        this.ensureNameNotTaken(nombreEmpresa);

        this.empresasService.save(nombreEmpresa, jugador, descripcion);

        this.eventBus.publish(new EmpresaCreadaEvento(jugador, nombreEmpresa));
    }

    private void ensureNombreEmpresaCorrectFormat(String nombreEmpresa){
        if(nombreEmpresa == null || nombreEmpresa.length() <= 0 || nombreEmpresa.length() > EmpresasService.MAX_NOMBRE_LONGITUD)
            throw new IllegalLength("El nombre tiene que comprender entre 1 y 16 caracteres");
    }

    private void ensureDescripccionCorrectFormat(String descripccion){
        if(descripccion == null || descripccion.length() <= 0 || descripccion.length() > EmpresasService.MAX_DESC_LONGITUD)
            throw new IllegalLength("La descripccion tiene que comprender entre 1 y 200 caracteres");
    }

    private void ensureNameNotTaken(String nombre){
        var taken = (Try.of(() -> this.empresasService.getByNombre(nombre))).isSuccess();

        if(taken)
            throw new AlreadyExists("El nombre ya esta cogido");
    }

    private void ensureUserNotPassMaxEmpresas(String jugadorNombre){
        if(this.empresasService.getByOwner(jugadorNombre).size() > EmpresasService.MAX_EMPRESAS_PER_JUGADOR)
            throw new IllegalQuantity("No puedes tener mas de " + EmpresasService.MAX_EMPRESAS_PER_JUGADOR + " empresas");
    }
}
