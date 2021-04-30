package es.serversurvival.bolsa.ofertasmercadoserver.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class OnJugadorCambiadoNombre implements AllMySQLTablesInstances {
    @EventListener
    public void on (JugadorCambiadoDeNombreEvento evento) {
        ofertasMercadoServerMySQL.cambiarNombreJugador(evento.getAntiguoNombre(), evento.getNuevoNombre());
    }
}
