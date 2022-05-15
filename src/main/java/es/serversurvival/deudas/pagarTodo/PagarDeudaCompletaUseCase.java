package es.serversurvival.deudas.pagarTodo;

import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.deudas._shared.application.DeudasService;
import es.serversurvival.deudas._shared.domain.Deuda;
import es.serversurvival.Pixelcoin;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;

import java.util.UUID;

public final class PagarDeudaCompletaUseCase {
    private final DeudasService deudasService;
    private final JugadoresService jugadoresService;

    public PagarDeudaCompletaUseCase() {
        this.deudasService = DependecyContainer.get(DeudasService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    public Deuda pagarDeuda(UUID deudaId, String deudorName) {
        var deudaAPagar = this.ensureDeudaExists(deudaId);
        this.ensureIsDeudor(deudaAPagar, deudorName);
        var deudorJugador = this.ensureDeudorHasEnoughPixelcoins(deudaAPagar);

        int pixelcoinsDeuda = deudaAPagar.getPixelcoins_restantes();
        String acredorNombre = deudaAPagar.getAcredor();
        Jugador acredor = jugadoresService.getByNombre(deudaAPagar.getAcredor());

        this.makeTransference(deudaAPagar, acredor, deudorJugador);
        this.deudasService.deleteById(deudaId);

        Pixelcoin.publish(new DeudaPagadaCompletaEvento(acredorNombre, deudaAPagar.getDeudor(), pixelcoinsDeuda));

        return deudaAPagar;
    }

    private Jugador ensureDeudorHasEnoughPixelcoins(Deuda deuda){
        Jugador jugadorDeudor = this.jugadoresService.getByNombre(deuda.getDeudor());

        if(jugadorDeudor.getPixelcoins() < deuda.getPixelcoins_restantes())
            throw new NotEnoughPixelcoins("No tienes las suficientes pixelcoins para pagar la dueda");

        return jugadorDeudor;
    }

    private Deuda ensureDeudaExists(UUID deudaId){
        return this.deudasService.getDeudaById(deudaId);
    }

    private void ensureIsDeudor(Deuda deuda, String deudorName){
        if(!deuda.getDeudor().equalsIgnoreCase(deudorName))
            throw new NotTheOwner("No eres el deudor de la deuda");
    }

    private void makeTransference(Deuda deudaAPagar, Jugador acredor, Jugador deudorJugador) {
        this.jugadoresService.realizarTransferenciaConEstadisticas(
                deudorJugador, acredor, deudorJugador.getPixelcoins()
        );
    }

}
