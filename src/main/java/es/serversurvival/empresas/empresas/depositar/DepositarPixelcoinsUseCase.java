package es.serversurvival.empresas.empresas.depositar;

import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;

public final class DepositarPixelcoinsUseCase {
    private final JugadoresService jugadoresService;
    private final EmpresasService empresasService;

    public DepositarPixelcoinsUseCase() {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.empresasService = DependecyContainer.get(EmpresasService.class);
    }

    public void depositar (String empresaNomrbeADepositar, String jugadorNombre, double pixelcoins) {
        this.ensureCorrectFormatPixelcoins(pixelcoins);
        Jugador jugador = this.ensureEnoughPixelcoins(jugadorNombre, pixelcoins);
        var empresaADepositoar = this.ensureEmpresasExists(empresaNomrbeADepositar);
        this.ensureOwnerOfEmpresa(empresaADepositoar, jugadorNombre);

        this.empresasService.save(empresaADepositoar.incrementPixelcoinsBy(pixelcoins));
        this.jugadoresService.save(jugador.decrementPixelcoinsBy(pixelcoins));

        Pixelcoin.publish(new PixelcoinsDepositadasEvento(jugador, empresaADepositoar, pixelcoins));
    }


    private void ensureCorrectFormatPixelcoins(double pixelcoins){
        if(pixelcoins <= 0)
            throw new IllegalQuantity("Las pixelcions ha depositar han de ser positivas");
    }

    private Jugador ensureEnoughPixelcoins(String jugadorNombre, double pixelcoinsADepositar){
        var jugador = this.jugadoresService.getByNombre(jugadorNombre);

        if(jugador.getPixelcoins() > pixelcoinsADepositar)
            throw new NotEnoughPixelcoins("No tienes las suficientes pixelcoins para depositar");

        return jugador;
    }

    private Empresa ensureEmpresasExists(String empresaNombre){
        return this.empresasService.getEmpresaByNombre(empresaNombre);
    }

    private void ensureOwnerOfEmpresa(Empresa empresa, String jugador){
        if(!empresa.getOwner().equals(jugador))
            throw new NotTheOwner("No eres el owner de la empresa, si quiers dar pixelcoins a esta, debes comprar un servicio suyo /empresas help");
    }
}
