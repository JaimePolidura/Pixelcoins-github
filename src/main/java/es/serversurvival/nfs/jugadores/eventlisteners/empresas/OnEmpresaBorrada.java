package es.serversurvival.nfs.jugadores.eventlisteners.empresas;

import es.jaime.EventListener;
import es.serversurvival.nfs.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.nfs.jugadores.mySQL.Jugador;
import es.serversurvival.nfs.empresas.borrar.EmpresaBorradaEvento;

public final class OnEmpresaBorrada implements AllMySQLTablesInstances {
    @EventListener
    public void onEmpresaBorrada (EmpresaBorradaEvento evento) {
        Jugador owner = jugadoresMySQL.getJugador(evento.getJugador());

        jugadoresMySQL.setPixelcoin(owner.getNombre(), owner.getPixelcoins() + evento.getPixelcoins());
    }
}
