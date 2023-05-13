package es.serversurvival.deudas.pagarTodo;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.deudas._shared.application.DeudasService;
import es.serversurvival.deudas._shared.domain.Deuda;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import lombok.AllArgsConstructor;

import java.util.UUID;

@UseCase
@AllArgsConstructor
public final class PagarDeudaCompletaUseCase {
    private final DeudasService deudasService;
    private final JugadoresService jugadoresService;
    private final EventBus eventBus;

    public void pagarDeuda(UUID deudaId, String deudorName) {
        var deudaAPagar = this.ensureDeudaExists(deudaId);
        this.ensureIsDeudor(deudaAPagar, deudorName);
        var deudorJugador = this.ensureDeudorHasEnoughPixelcoins(deudaAPagar);

        String acredorNombre = deudaAPagar.getAcredor();
        Jugador acredorJugador = jugadoresService.getByNombre(deudaAPagar.getAcredor());

        this.jugadoresService.realizarTransferencia(deudorJugador, acredorJugador, deudaAPagar.getPixelcoinsRestantes());
        this.deudasService.deleteById(deudaId);

        this.eventBus.publish(new DeudaPagadaCompletaEvento(acredorNombre, deudaAPagar.getDeudor(), deudaAPagar.getPixelcoinsRestantes()));

    }

    private Jugador ensureDeudorHasEnoughPixelcoins(Deuda deuda){
        Jugador jugadorDeudor = this.jugadoresService.getByNombre(deuda.getDeudor());

        if(jugadorDeudor.getPixelcoins() < deuda.getPixelcoinsRestantes())
            throw new NotEnoughPixelcoins("No tienes las suficientes pixelcoins para pagar la dueda");

        return jugadorDeudor;
    }

    private Deuda ensureDeudaExists(UUID deudaId){
        return this.deudasService.getById(deudaId);
    }

    private void ensureIsDeudor(Deuda deuda, String deudorName){
        if(!deuda.getDeudor().equalsIgnoreCase(deudorName))
            throw new NotTheOwner("No eres el deudor de la deuda");
    }
}
