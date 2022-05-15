package es.serversurvival.bolsa.other._shared.ofertasmercadoserver.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;

public final class OnJugadorCambiadoNombre implements AllMySQLTablesInstances {
    @EventListener
    public void on (JugadorCambiadoDeNombreEvento evento) {
        ofertasMercadoServerMySQL.cambiarNombreJugador(evento.getAntiguoNombre(), evento.getNuevoNombre());
    }
}
