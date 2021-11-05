package es.serversurvival.jugadores.pagar;

import es.serversurvival.Pixelcoin;
import es.serversurvival.jugadores._shared.mySQL.Jugadores;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class PagarUseCase implements AllMySQLTablesInstances {
    public void realizarPagoManual(String nombrePagador, String nombrePagado, double cantidad) {
        Jugadores.INSTANCE.realizarTransferenciaConEstadisticas(nombrePagador, nombrePagado, cantidad);

        Pixelcoin.publish(new JugadorPagoManualEvento(nombrePagador, nombrePagado, cantidad));
    }
}
