package es.serversurvival.bolsa.other._shared.llamadasapi.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival._shared.eventospixelcoins.PosicionCerradaEvento;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class OnPosicionCerrada implements AllMySQLTablesInstances {
    @EventListener
    public void on (PosicionCerradaEvento e) {
        llamadasApiMySQL.borrarLlamadaSiNoEsUsada(e.getTicker());
    }
}
