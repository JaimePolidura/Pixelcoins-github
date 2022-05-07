package es.serversurvival.deudas.prestar;

import es.jaime.javaddd.domain.exceptions.CannotBeYourself;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.deudas._shared.newformat.application.DeudasService;
import es.serversurvival.jugadores._shared.newformat.application.JugadoresService;
import es.serversurvival.jugadores._shared.newformat.domain.Jugador;

public final class PrestarUseCase implements AllMySQLTablesInstances {
    private final JugadoresService jugadoresService;
    private final DeudasService deudasService;

    public PrestarUseCase () {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.deudasService = DependecyContainer.get(DeudasService.class);
    }

    public void prestar (String acredor, String deudor, int pixelcoins, int interes, int dias) {
        this.ensureNotTheSame(acredor, deudor);
        Jugador acredorJugador = this.ensureJugadorExists(acredor);
        Jugador deudorJugador = this.ensureJugadorExists(deudor);
        this.ensurePixelcionsAndDiasPositiveAndNotBigger(pixelcoins, dias, interes);
        int pixelcoinsMasInteres = Funciones.aumentarPorcentaje(pixelcoins, interes);
        this.ensureAcredorHasEnoughPixelcoins(acredorJugador, pixelcoinsMasInteres);

        this.deudasService.save(deudor, acredor, pixelcoins, dias, interes);
        this.jugadoresService.realizarTransferenciaConEstadisticas(
                acredorJugador, deudorJugador, pixelcoinsMasInteres
        );

        Pixelcoin.publish(new PixelcoinsPrestadasEvento(acredor, deudor, pixelcoins, interes, dias));
    }

    private void ensureAcredorHasEnoughPixelcoins(Jugador acredor, int pixelcoinsMasInteres) {
        if(acredor.getPixelcoins() < pixelcoinsMasInteres){
            throw new NotEnoughPixelcoins("No tienes las suficientes pixelcoins para hacer el prestamo");
        }
    }

    private void ensurePixelcionsAndDiasPositiveAndNotBigger(int pixelcoins, int dias, int interes) {
        if(pixelcoins <= 0 || dias <= 0 || interes < 0)
            throw new IllegalQuantity("Los dias, las pixelcoins y los intereses tienen que ser numeros naturales");

        if(dias < pixelcoins )
            throw new IllegalQuantity("Los dias tienen que ser mayores que las pixelcoins");
    }

    private void ensureNotTheSame(String acredor, String deudor) {
        if(acredor.equalsIgnoreCase(deudor))
            throw new CannotBeYourself("No puedes autoprestarte dinero");
    }

    private Jugador ensureJugadorExists(String jugadorName) {
        return this.jugadoresService.getJugadorByNombre(jugadorName);
    }
}
