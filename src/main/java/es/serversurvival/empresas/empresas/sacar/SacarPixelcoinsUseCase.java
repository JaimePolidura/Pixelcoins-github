package es.serversurvival.empresas.empresas.sacar;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaimetruman.annotations.UseCase;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@UseCase
public final class SacarPixelcoinsUseCase {
    private final EmpresasService empresasService;
    private final JugadoresService jugadoresService;
    private final EventBus eventBus;

    public SacarPixelcoinsUseCase(){
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.eventBus = DependecyContainer.get(EventBus.class);
    }

    public void sacar (String playerName, String empresaNombre, double pixelcoinsASacar) {
        this.ensureCorrectFormatPixelcoins(pixelcoinsASacar);
        Jugador jugador = this.jugadoresService.getByNombre(playerName);
        Empresa empresaASacar = this.empresasService.getByNombre(empresaNombre);
        this.ensureOwnerOfEmpresa(empresaASacar, playerName);
        this.ensureHasEnoughPixelcions(empresaASacar, pixelcoinsASacar);

        this.empresasService.save(empresaASacar.decrementPixelcoinsBy(pixelcoinsASacar));
        this.jugadoresService.save(jugador.incrementPixelcoinsBy(pixelcoinsASacar));

        this.eventBus.publish(new PixelcoinsSacadasEvento(jugador.getNombre(), empresaASacar.getNombre(), pixelcoinsASacar));
    }


    private void ensureHasEnoughPixelcions(Empresa empresa, double pixelcoins){
        if(empresa.getPixelcoins() < pixelcoins)
            throw new NotEnoughPixelcoins("La empresa notiene las suficientes pixelcoins para sacar");
    }

    private void ensureOwnerOfEmpresa(Empresa empresa, String playerName){
        if(!empresa.getOwner().equalsIgnoreCase(playerName))
            throw new NotTheOwner("No eres el owner de la empresa");
    }

    private void ensureCorrectFormatPixelcoins(double pixelcoins){
        if(pixelcoins <= 0)
            throw new IllegalQuantity("Las pixelcoins deben de ser un numero natural");
    }
}
