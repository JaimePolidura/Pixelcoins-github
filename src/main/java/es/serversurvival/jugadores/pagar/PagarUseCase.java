package es.serversurvival.jugadores.pagar;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.CannotBeYourself;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class PagarUseCase {
    private final JugadoresService jugadoresService;
    private final EventBus eventBus;

    public PagarUseCase(){
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.eventBus = DependecyContainer.get(EventBus.class);
    }

    public void realizarPago(String nombrePagador, String nombrePagado, double pixelcoins) {
        this.ensureNotSamePlayer(nombrePagador, nombrePagado);
        this.ensureCorrectFormatPixelcoins(pixelcoins);
        var jugadorPagado = this.ensureJugadorExists(nombrePagado);
        var jugadorPagador = this.ensureJugadorExists(nombrePagador);
        this.ensureEnoughPixelcoins(jugadorPagador, pixelcoins);

        this.jugadoresService.realizarTransferencia(jugadorPagador, jugadorPagado, pixelcoins);

        this.eventBus.publish(new JugadorPagoManualEvento(nombrePagador, nombrePagado, pixelcoins));
    }

    private void ensureNotSamePlayer(String pagador, String pagado){
        if(pagador.equals(pagado)){
            throw new CannotBeYourself("No te pudes pagar a ti mismo");
        }
    }

    private void ensureCorrectFormatPixelcoins(double pixelcoins) {
        if(pixelcoins <= 0){
            throw new IllegalQuantity("Las pixelcoins tienen que ser un numero positivo superior a 0");
        }
    }

    private void ensureEnoughPixelcoins(Jugador jugador, double pixelcoins){
        if(pixelcoins > jugador.getPixelcoins())
            throw new NotEnoughPixelcoins("No tienes las suficientes pixelcoins para realizar el pago");
    }

    private Jugador ensureJugadorExists(String nombre){
        return this.jugadoresService.getByNombre(nombre);
    }
}
