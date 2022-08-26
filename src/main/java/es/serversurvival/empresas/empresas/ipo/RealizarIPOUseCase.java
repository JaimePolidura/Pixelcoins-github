package es.serversurvival.empresas.empresas.ipo;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.IllegalState;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaimetruman.annotations.UseCase;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.accionistasserver._shared.application.AccionistasServerService;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import lombok.AllArgsConstructor;

import java.util.UUID;

import static es.serversurvival.empresas.accionistasserver._shared.domain.TipoAccionista.*;

@AllArgsConstructor
@UseCase
public final class RealizarIPOUseCase {
    private final EmpresasService empresasService;
    private final OfertasAccionesServerService ofertasAccionesServerService;
    private final AccionistasServerService accionistasEmpresasServerService;
    private final EventBus eventBus;

    public RealizarIPOUseCase() {
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.ofertasAccionesServerService = DependecyContainer.get(OfertasAccionesServerService.class);
        this.accionistasEmpresasServerService = DependecyContainer.get(AccionistasServerService.class);
        this.eventBus = DependecyContainer.get(EventBus.class);
    }

    public void makeIPO(String playerName, IPOCommand command){
        this.ensureNaturalNumber(command.getAccionesOwner());
        this.ensureNaturalNumber((int) command.getPrecioPorAccion());
        this.ensureNaturalNumber(command.getAccionesTotales());
        this.ensureMinValue(command.getAccionesOwner(), 1);
        this.ensureMinValue((int) command.getPrecioPorAccion(), 1);
        this.ensureMinValue(command.getAccionesTotales(), 2);
        this.ensureMaxValue(command.getAccionesOwner(), command.getAccionesTotales());
        var empresaToIPO = this.empresasService.getByNombre(command.getEmpresa());
        this.ensureOwner(empresaToIPO, playerName);
        this.ensureNotAlreadyCotizada(empresaToIPO);

        int cantidadAVender = command.getAccionesTotales() - command.getAccionesOwner();
        UUID accionistaEmpresaId = this.accionistasEmpresasServerService.save(command.getEmpresa(), EMPRESA, command.getEmpresa(),
                cantidadAVender, command.getPrecioPorAccion());
        this.ofertasAccionesServerService.save(command.getEmpresa(), command.getEmpresa(), command.getPrecioPorAccion(),
                cantidadAVender, EMPRESA, command.getPrecioPorAccion(), accionistaEmpresaId);

        this.accionistasEmpresasServerService.save(playerName, JUGADOR, command.getEmpresa(), command.getAccionesOwner(),
                 command.getPrecioPorAccion());

        this.empresasService.save(empresaToIPO.setCotizadaToTrue()
                .withAccionesTotales(command.getAccionesTotales()));

        this.eventBus.publish(IPORealizada.of(empresaToIPO.getNombre(), command.getPrecioPorAccion(),
                command.getAccionesTotales(), command.getAccionesOwner()));
    }

    private void ensureNotAlreadyCotizada(Empresa empresa){
        if(empresa.isCotizada())
            throw new IllegalState("La empresa ya es cotizada");
    }

    private void ensureOwner(Empresa empresaToIPO, String owner){
        if(!empresaToIPO.getOwner().equalsIgnoreCase(owner))
            throw new NotTheOwner("No eres el owner de la empresa");
    }

    private void ensureMaxValue(int number, int max){
        if(number >= max)
            throw new IllegalQuantity("El numero no puede ser mayor o igual que " + max);
    }

    private void ensureMinValue(int number, int minValue){
        if(number < minValue)
            throw new IllegalQuantity("Numero incorrecto, no puede ser menor que " + number);
    }

    private void ensureNaturalNumber(int number){
        if(number <= 0)
            throw new IllegalQuantity("El numero ha de ser natural");
    }
}
