package es.serversurvival.empresas.empresas.comprarservicio;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.CannotBeYourself;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class ComprarServicioUseCase {
    private final EmpresasService empresasService;
    private final JugadoresService jugadoresService;
    private final EventBus eventBus;

    public ComprarServicioUseCase() {
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.eventBus = DependecyContainer.get(EventBus.class);
    }

    public Empresa comprar (String jugador, String empresa, double pixelcoins) {
        this.ensurePixelcoinsCorrectFormat(pixelcoins);
        Empresa empresaAComprarServicio = empresasService.getByNombre(empresa);
        this.ensureNotOwnerOfEmpresa(jugador, empresaAComprarServicio);
        var jugadorComprador = this.ensureEnoughPixelcoins(jugador, pixelcoins);
        
        this.empresasService.save(empresaAComprarServicio.incrementPixelcoinsBy(pixelcoins)
                .incrementIngresosBy(pixelcoins));

        this.jugadoresService.save(jugadorComprador.decrementPixelcoinsBy(pixelcoins)
                .incrementGastosBy(pixelcoins));

        this.eventBus.publish(new EmpresaServicioCompradoEvento(jugador, empresaAComprarServicio, pixelcoins));

        return empresaAComprarServicio;
    }

    private void ensureNotOwnerOfEmpresa(String jugadorNombre, Empresa empresa){
        if(jugadorNombre.equalsIgnoreCase(empresa.getOwner()))
            throw new CannotBeYourself("Eres el owner de la empresa, no puedes autocomprarte nada");
    }

    private Jugador ensureEnoughPixelcoins(String jugadorNombre, double pixelcoins) {
        var jugador = this.jugadoresService.getByNombre(jugadorNombre);

        if(jugador.getPixelcoins() < pixelcoins)
            throw new NotEnoughPixelcoins("No tienes las suficientes pixelcoins");

        return jugador;
    }

    private void ensurePixelcoinsCorrectFormat(double pixelcions){
        if(pixelcions <= 0)
            throw new IllegalQuantity("Las pixelcoins han de ser positivas");
    }
}
