package es.serversurvival.jugadores._shared.eventlisteners.empresas;

import es.jaime.EventListener;
import es.serversurvival.empresas.borrar.EmpresaBorradaEvento;
import es.serversurvival.jugadores._shared.newformat.domain.Jugador;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class OnEmpresaBorrada implements AllMySQLTablesInstances {
    @EventListener
    public void onEmpresaBorrada (EmpresaBorradaEvento evento) {
        Jugador owner = jugadoresMySQL.getJugador(evento.getJugador());

        jugadoresMySQL.setPixelcoin(owner.getNombre(), owner.getPixelcoins() + evento.getPixelcoins());
    }
}
