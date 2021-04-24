package es.serversurvival.nfs.jugadores.eventlisteners.empresas;

import es.jaime.EventListener;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.tablasObjetos.Jugador;
import es.serversurvival.nfs.empresas.borrar.EmpresaBorradaEvento;

public final class OnEmpresaBorrada implements AllMySQLTablesInstances {
    @EventListener
    public void onEmpresaBorrada (EmpresaBorradaEvento evento) {
        Jugador owner = jugadoresMySQL.getJugador(evento.getJugador());

        jugadoresMySQL.setPixelcoin(owner.getNombre(), owner.getPixelcoins() + evento.getPixelcoins());
    }
}
