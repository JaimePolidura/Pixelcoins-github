package es.serversurvival.jugadores.eventlisteners.empresas;

import es.jaime.EventListener;
import es.serversurvival.empresas.comprarservicio.EmpresaServicioCompradoEvento;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.jugadores.mySQL.Jugador;

public final class OnEmpresaServicioComprado implements AllMySQLTablesInstances {
    @EventListener
    public void on (EmpresaServicioCompradoEvento evento) {
        Jugador jugador = jugadoresMySQL.getJugador(evento.getComprador());
        double precio = evento.getPrecio();

        jugadoresMySQL.setEstadisticas(jugador.getNombre(), jugador.getPixelcoins() - precio, jugador.getNventas(), jugador.getIngresos(), jugador.getGastos() + precio);
    }
}
