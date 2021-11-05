package es.serversurvival.transacciones.eventlisteners;

import es.jaime.EventListener;
import es.jaime.TransacionalEventListenerNotImplemented;
import es.jaime.TransactionalEventListener;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class OnJugadorCambiadoNombre implements AllMySQLTablesInstances {
    @EventListener
    public void on(JugadorCambiadoDeNombreEvento evento) {
        transaccionesMySQL.setCompradorVendedor(evento.getAntiguoNombre(), evento.getNuevoNombre());
    }
}
