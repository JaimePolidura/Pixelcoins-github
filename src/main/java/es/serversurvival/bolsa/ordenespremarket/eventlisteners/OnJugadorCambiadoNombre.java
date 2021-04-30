package es.serversurvival.bolsa.ordenespremarket.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class OnJugadorCambiadoNombre implements AllMySQLTablesInstances {
    @EventListener
    public void on (JugadorCambiadoDeNombreEvento evento) {
        ordenesMySQL.cambiarNombreJugador(evento.getAntiguoNombre(), evento.getNuevoNombre());
    }
}
