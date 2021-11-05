package es.serversurvival.bolsa._shared.llamadasapi.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.shared.eventospixelcoins.PosicionCerradaEvento;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class OnPosicionCerrada implements AllMySQLTablesInstances {
    @EventListener
    public void on (PosicionCerradaEvento e) {
        llamadasApiMySQL.borrarLlamadaSiNoEsUsada(e.getTicker());
    }
}
