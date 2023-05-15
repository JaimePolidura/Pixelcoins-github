package es.serversurvival.v1.empresas.empresas.depositar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.v1._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.v1.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.v1.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import es.serversurvival.v1.jugadores._shared.domain.Jugador;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@UseCase
public final class DepositarPixelcoinsUseCase {
    private final JugadoresService jugadoresService;
    private final EmpresasService empresasService;
    private final EventBus eventBus;

    public void depositar (String empresaNomrbeADepositar, String jugadorNombre, double pixelcoins) {
        this.ensureCorrectFormatPixelcoins(pixelcoins);
        Jugador jugador = this.ensureEnoughPixelcoins(jugadorNombre, pixelcoins);
        var empresaADepositoar = this.ensureEmpresasExists(empresaNomrbeADepositar);
        this.ensureOwnerOfEmpresa(empresaADepositoar, jugadorNombre);

        this.empresasService.save(empresaADepositoar.incrementPixelcoinsBy(pixelcoins));
        this.jugadoresService.save(jugador.decrementPixelcoinsBy(pixelcoins));

        this.eventBus.publish(new PixelcoinsDepositadasEvento(jugador.decrementPixelcoinsBy(pixelcoins),
                empresaADepositoar.incrementPixelcoinsBy(5), pixelcoins));
    }


    private void ensureCorrectFormatPixelcoins(double pixelcoins){
        if(pixelcoins <= 0)
            throw new IllegalQuantity("Las pixelcions ha depositar han de ser positivas");
    }

    private Jugador ensureEnoughPixelcoins(String jugadorNombre, double pixelcoinsADepositar){
        var jugador = this.jugadoresService.getByNombre(jugadorNombre);

        if(jugador.getPixelcoins() < pixelcoinsADepositar)
            throw new NotEnoughPixelcoins("No tienes las suficientes pixelcoins para depositar");

        return jugador;
    }

    private Empresa ensureEmpresasExists(String empresaNombre){
        return this.empresasService.getByNombre(empresaNombre);
    }

    private void ensureOwnerOfEmpresa(Empresa empresa, String jugador){
        if(!empresa.getOwner().equals(jugador))
            throw new NotTheOwner("No eres el owner de la empresa, si quiers dar pixelcoins a esta, debes comprar un servicio suyo /empresas help");
    }
}
