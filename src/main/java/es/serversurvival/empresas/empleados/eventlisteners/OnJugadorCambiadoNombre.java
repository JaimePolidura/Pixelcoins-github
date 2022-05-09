package es.serversurvival.empresas.empleados.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class OnJugadorCambiadoNombre implements AllMySQLTablesInstances {
    @EventListener
    public void on (JugadorCambiadoDeNombreEvento evento) {
        empleadosMySQL.setEmpleado(evento.getAntiguoNombre(), evento.getNuevoNombre());
    }
}
