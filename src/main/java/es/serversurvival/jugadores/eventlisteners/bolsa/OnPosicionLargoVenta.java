package es.serversurvival.jugadores.eventlisteners.bolsa;

import es.jaime.EventListener;
import es.serversurvival.bolsa.posicionesabiertas.venderlargo.PosicionVentaLargoEvento;
import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class OnPosicionLargoVenta implements AllMySQLTablesInstances {
    @EventListener
    public void onPosicionLargaVenta (PosicionVentaLargoEvento evento){
        Jugador vendedor = jugadoresMySQL.getJugador(evento.getVendedor());
        double beneficiosPerdidas = evento.getResultado();

        if(beneficiosPerdidas >= 0)
            jugadoresMySQL.setEstadisticas(vendedor.getNombre(), vendedor.getPixelcoins() + evento.getValorTotal(), vendedor.getNventas(),
                    vendedor.getIngresos() + beneficiosPerdidas, vendedor.getGastos());
        else
            jugadoresMySQL.setEstadisticas(vendedor.getNombre(), vendedor.getPixelcoins() + evento.getValorTotal(), vendedor.getNventas(),
                    vendedor.getIngresos(), vendedor.getGastos() + beneficiosPerdidas);
    }

}
