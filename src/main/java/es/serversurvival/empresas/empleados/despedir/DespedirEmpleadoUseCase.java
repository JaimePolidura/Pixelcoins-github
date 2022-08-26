package es.serversurvival.empresas.empleados.despedir;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.CannotBeYourself;
import es.jaime.javaddd.domain.exceptions.IllegalLength;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaimetruman.annotations.UseCase;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import lombok.AllArgsConstructor;

import static es.serversurvival._shared.utils.Funciones.enviarMensaje;
import static es.serversurvival.empresas.empleados._shared.application.EmpleadosService.*;

@AllArgsConstructor
@UseCase
public final class DespedirEmpleadoUseCase {
    private final EmpresasService empresasService;
    private final EmpleadosService empleadosService;
    private final EventBus eventBus;

    public DespedirEmpleadoUseCase() {
        this.empleadosService = DependecyContainer.get(EmpleadosService.class);
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.eventBus = DependecyContainer.get(EventBus.class);
    }

    public void despedir (String owner, String empleadoNombreADespedir, String empresaNombre, String razon) {
        this.ensureNotHisSelf(owner, empleadoNombreADespedir);
        this.ensureRazonCorrectFormat(razon);
        var empresa = this.empresasService.getByNombre(empresaNombre);
        this.ensureOwnerOfEmpresa(empresa, owner);
        var empeladoADespedir = this.ensureEmpeladoWorks(empresaNombre, empleadoNombreADespedir);

        this.empleadosService.deleteById(empeladoADespedir.getEmpleadoId());

        this.eventBus.publish(new EmpleadoDespedido(empleadoNombreADespedir, empresaNombre, razon));
    }

    private Empleado ensureEmpeladoWorks(String empresa, String empleado){
        return this.empleadosService.getEmpleadoInEmpresa(empleado, empresa);
    }

    private void ensureOwnerOfEmpresa(Empresa empresa, String owner) {
        if(!empresa.getOwner().equalsIgnoreCase(owner))
            throw new NotTheOwner("No eres el owner de la empresa");
    }

    private void ensureRazonCorrectFormat(String razon){
        if(razon == null || razon.length() < MIN_DESPEDIR_RAZON_LENGH || razon.length() > MAX_DESPEDIR_RAZON_LENGH)
            throw new IllegalLength("Razon de despido invalido, debe ocupar entre " + MIN_DESPEDIR_RAZON_LENGH
                    + " y " + MAX_DESPEDIR_RAZON_LENGH + " caracteres");
    }

    private void ensureNotHisSelf(String owner, String jugadorADespedir){
        if(owner.equalsIgnoreCase(jugadorADespedir))
            throw new CannotBeYourself("No te puedes contratar a ti mismo");
    }
}
