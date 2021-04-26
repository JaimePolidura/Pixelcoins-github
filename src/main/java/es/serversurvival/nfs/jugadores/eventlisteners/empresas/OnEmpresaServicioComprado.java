package es.serversurvival.nfs.jugadores.eventlisteners.empresas;

import es.jaime.EventListener;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.nfs.jugadores.mySQL.Jugador;
import es.serversurvival.nfs.empresas.comprarservicio.EmpresaServicioCompradoEvento;

public final class OnEmpresaServicioComprado implements AllMySQLTablesInstances {
    @EventListener
    public void on (EmpresaServicioCompradoEvento evento) {
        Jugador jugador = jugadoresMySQL.getJugador(evento.getComprador());
        double precio = evento.getPrecio();

        jugadoresMySQL.setEstadisticas(jugador.getNombre(), jugador.getPixelcoins() - precio, jugador.getNventas(), jugador.getIngresos(), jugador.getGastos() + precio);
    }
}
