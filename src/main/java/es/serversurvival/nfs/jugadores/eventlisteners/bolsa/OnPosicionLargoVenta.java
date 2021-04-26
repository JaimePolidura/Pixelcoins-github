package es.serversurvival.nfs.jugadores.eventlisteners.bolsa;

import es.jaime.EventListener;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.eventos.bolsa.PosicionVentaLargoEvento;
import es.serversurvival.nfs.jugadores.mySQL.Jugador;

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
