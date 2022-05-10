package es.serversurvival.jugadores.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.venderlargo.PosicionVentaLargoEvento;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class OnPosicionLargoVenta implements AllMySQLTablesInstances {
    private final JugadoresService jugadoresService;

    public OnPosicionLargoVenta() {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    @EventListener
    public void onPosicionLargaVenta (PosicionVentaLargoEvento evento){
        Jugador vendedor = jugadoresService.getByNombre(evento.getVendedor());
        double beneficiosPerdidas = evento.getResultado();

        if(beneficiosPerdidas >= 0)
            this.jugadoresService.save(vendedor.incrementPixelcoinsBy(evento.getValorTotal()).incrementIngresosBy(beneficiosPerdidas));
        else
            this.jugadoresService.save(vendedor.incrementPixelcoinsBy(evento.getValorTotal()).incrementGastosBy(beneficiosPerdidas));
    }

}
