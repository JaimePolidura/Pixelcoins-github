package es.serversurvival.jugadores._shared.eventlisteners.empresas;

import es.jaime.EventListener;
import es.serversurvival.empresas.vender.EmpresaVendedidaEvento;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class OnEmpresaVendida implements AllMySQLTablesInstances {
    @EventListener
    public void on (EmpresaVendedidaEvento evento) {
        jugadoresMySQL.realizarTransferenciaConEstadisticas(evento.getComprador(), evento.getVendedor(), evento.getPixelcoins());
    }
}
