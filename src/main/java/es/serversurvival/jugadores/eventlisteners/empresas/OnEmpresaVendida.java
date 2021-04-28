package es.serversurvival.jugadores.eventlisteners.empresas;

import es.jaime.EventListener;
import es.serversurvival.empresas.vender.EmpresaVendedidaEvento;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class OnEmpresaVendida implements AllMySQLTablesInstances {
    @EventListener
    public void on (EmpresaVendedidaEvento evento) {
        jugadoresMySQL.realizarTransferenciaConEstadisticas(comprador, vendedor, pixelcoins);
    }
}
