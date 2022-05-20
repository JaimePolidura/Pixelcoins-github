package es.serversurvival.empresas.empleados.contratar;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.*;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.TipoSueldo;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import lombok.AllArgsConstructor;

import static es.serversurvival.empresas.empleados._shared.application.EmpleadosService.*;

@AllArgsConstructor
public final class ContratarUseCase {
    private final EmpleadosService empleadosService;
    private final EmpresasService empresasService;
    private final EventBus eventBus;

    public ContratarUseCase() {
        this.empleadosService = DependecyContainer.get(EmpleadosService.class);
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.eventBus = DependecyContainer.get(EventBus.class);
    }

    public void contratar (String ownerEmpresa, String jugadorAContratar, String empresaNombre, double salario, TipoSueldo tipoSueldo, String cargo) {
        this.ensureNotHisSelf(ownerEmpresa, jugadorAContratar);
        this.ensureCargoCorrectFormat(cargo);
        this.ensureCorrectTipoSueldo(tipoSueldo);
        this.ensureCorrectFormatPixelcoins(salario);
        var empresa = this.ensureEmpresaExists(empresaNombre);
        this.ensureOwner(ownerEmpresa, empresa);
        this.ensureNotWorksForEmpresa(empresa, jugadorAContratar);

        this.empleadosService.save(jugadorAContratar, empresaNombre, salario, tipoSueldo, cargo);

        this.eventBus.publish(new JugadorContratado(jugadorAContratar, empresaNombre, cargo));
    }

    private void ensureNotHisSelf(String owner, String jugadorAContratar){
        if(owner.equalsIgnoreCase(jugadorAContratar))
            throw new CannotBeYourself("No te puedes contratar a ti mismo");
    }

    private void ensureCargoCorrectFormat(String cargo){
        if(cargo == null || cargo.length() < MIN_CARGO_LENGTH || cargo.length() > MAX_CARGO_LENGTH){
            throw new IllegalQuantity("La longitud del cargo debe de ser entre [0, 16] caracteres");
        }
    }

    private void ensureCorrectTipoSueldo(TipoSueldo tipoSueldo){
        if(tipoSueldo == null)
            throw new IllegalQuantity("El tipo sueldo no puede ser nulo, /empresa help");
    }

    private void ensureCorrectFormatPixelcoins(double pixelcoins){
        if(pixelcoins <= 0)
            throw new IllegalQuantity("El sueldo debe de ser superior a 0");
    }

    private void ensureNotWorksForEmpresa(Empresa empresa, String jugadorAContratar){
        var alreadyWorks = this.empleadosService.findByEmpresa(empresa.getNombre()).stream()
                .anyMatch(empleoDeEmpresa -> empleoDeEmpresa.getNombre().equalsIgnoreCase(jugadorAContratar));

        if(alreadyWorks)
            throw new AlreadyExists("El jugador ya trabaja en tu empresa");
    }

    private Empresa ensureEmpresaExists(String empresaNombre) {
        return this.empresasService.getByNombre(empresaNombre);
    }

    private void ensureOwner(String owner, Empresa empresa){
        if(!empresa.getOwner().equalsIgnoreCase(owner))
            throw new NotTheOwner("El contratador no es el owner de la empresa");
    }
}
