package es.serversurvival.nfs.jugadores.pagar;

import es.serversurvival.legacy.main.Pixelcoin;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.eventos.jugadores.JugadorPagoManualEvento;
import es.serversurvival.nfs.jugadores.mySQL.Jugadores;

public final class PagarUseCase implements AllMySQLTablesInstances {
    public void realizarPagoManual(String nombrePagador, String nombrePagado, double cantidad) {
        Jugadores.INSTANCE.realizarTransferenciaConEstadisticas(nombrePagador, nombrePagado, cantidad);

        Pixelcoin.publish(new JugadorPagoManualEvento(nombrePagador, nombrePagado, cantidad));
    }
}
