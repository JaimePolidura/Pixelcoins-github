package es.serversurvival.v1.deudas.prestar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.CannotBeYourself;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.serversurvival.v1._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.v1.deudas._shared.application.DeudasService;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import es.serversurvival.v1.jugadores._shared.domain.Jugador;
import lombok.AllArgsConstructor;

import static es.serversurvival.v1._shared.utils.Funciones.*;

@UseCase
@AllArgsConstructor
public final class PrestarUseCase {
    private final JugadoresService jugadoresService;
    private final DeudasService deudasService;
    private final EventBus eventBus;

    public void prestar (String acredor, String deudor, double pixelcoins, int interes, int dias) {
        this.ensureNotTheSame(acredor, deudor);
        Jugador acredorJugador = this.jugadoresService.getByNombre(acredor);
        Jugador deudorJugador = this.jugadoresService.getByNombre(deudor);
        this.ensurePixelcionsAndDiasPositiveAndNotBigger(pixelcoins, dias, interes);
        double pixelcoinsMasInteres = redondeoDecimales(aumentarPorcentaje(pixelcoins, interes), 2);

        this.ensureAcredorHasEnoughPixelcoins(acredorJugador, pixelcoinsMasInteres);

        this.deudasService.save(deudor, acredor, pixelcoinsMasInteres, dias, interes);
        this.jugadoresService.realizarTransferencia(
                acredorJugador, deudorJugador, pixelcoinsMasInteres
        );

        this.eventBus.publish(new PixelcoinsPrestadasEvento(acredor, deudor, pixelcoins, interes, dias));
    }

    private void ensureAcredorHasEnoughPixelcoins(Jugador acredor, double pixelcoinsMasInteres) {
        if(acredor.getPixelcoins() < pixelcoinsMasInteres)
            throw new NotEnoughPixelcoins("No tienes las suficientes pixelcoins para hacer el prestamo");
    }

    private void ensurePixelcionsAndDiasPositiveAndNotBigger(double pixelcoins, int dias, int interes) {
        if(pixelcoins <= 0 || dias <= 0 || interes < 0)
            throw new IllegalQuantity("Los dias, las pixelcoins y los intereses tienen que ser numeros naturales");
    }

    private void ensureNotTheSame(String acredor, String deudor) {
        if(acredor.equalsIgnoreCase(deudor))
            throw new CannotBeYourself("No puedes autoprestarte dinero");
    }
}
