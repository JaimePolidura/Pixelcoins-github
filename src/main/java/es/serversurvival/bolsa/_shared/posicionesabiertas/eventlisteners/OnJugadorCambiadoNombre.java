package es.serversurvival.bolsa._shared.posicionesabiertas.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class OnJugadorCambiadoNombre implements AllMySQLTablesInstances {
    @EventListener
    public void on (JugadorCambiadoDeNombreEvento evento) {
        posicionesAbiertasMySQL.setJugador(evento.getAntiguoNombre(), evento.getNuevoNombre());
    }
}
