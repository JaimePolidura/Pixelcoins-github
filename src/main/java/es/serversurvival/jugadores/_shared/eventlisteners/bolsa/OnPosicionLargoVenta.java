package es.serversurvival.jugadores._shared.eventlisteners.bolsa;

import es.jaime.EventListener;
import es.serversurvival.bolsa.venderlargo.PosicionVentaLargoEvento;
import es.serversurvival.jugadores._shared.newformat.domain.Jugador;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class OnPosicionLargoVenta implements AllMySQLTablesInstances {
    @EventListener
    public void onPosicionLargaVenta (PosicionVentaLargoEvento evento){
        Jugador vendedor = jugadoresMySQL.getJugador(evento.getVendedor());
        double beneficiosPerdidas = evento.getResultado();

        if(beneficiosPerdidas >= 0)
            jugadoresMySQL.setEstadisticas(vendedor.getNombre(), vendedor.getPixelcoins() + evento.getValorTotal(), vendedor.getNVentas(),
                    vendedor.getIngresos() + beneficiosPerdidas, vendedor.getGastos());
        else
            jugadoresMySQL.setEstadisticas(vendedor.getNombre(), vendedor.getPixelcoins() + evento.getValorTotal(), vendedor.getNVentas(),
                    vendedor.getIngresos(), vendedor.getGastos() + beneficiosPerdidas);
    }

}
