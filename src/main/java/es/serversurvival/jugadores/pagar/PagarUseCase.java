package es.serversurvival.jugadores.pagar;

import es.jaime.javaddd.domain.exceptions.CannotBeYourself;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import org.junit.runner.RunWith;

public final class PagarUseCase {
    private final JugadoresService jugadoresService;

    public PagarUseCase(JugadoresService jugadoresService){
        this.jugadoresService = jugadoresService;
    }

    public void realizarPago(String nombrePagador, String nombrePagado, double pixelcoins) {
        this.ensureNotSamePlayer(nombrePagador, nombrePagado);
        this.ensureCorrectFormatPixelcoins(pixelcoins);
        var jugadorPagado = this.ensureJugadorExists(nombrePagado);
        var jugadorPagador = this.ensureJugadorExists(nombrePagador);
        this.ensureEnoughPixelcoins(jugadorPagador, pixelcoins);

        this.jugadoresService.realizarTransferenciaConEstadisticas(jugadorPagador, jugadorPagado, pixelcoins);

        Pixelcoin.publish(new JugadorPagoManualEvento(nombrePagador, nombrePagado, pixelcoins));
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
