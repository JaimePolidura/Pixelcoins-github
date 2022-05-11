package es.serversurvival.bolsa.other._shared.llamadasapi.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival._shared.eventospixelcoins.PosicionAbiertaEvento;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class OnPosicionAbierta implements AllMySQLTablesInstances {
    @EventListener
    public void onOpenedPosition (PosicionAbiertaEvento e) {
        llamadasApiMySQL.nuevaLlamadaSiNoEstaReg(e.getTicker(), e.getPrecioUnidad(), e.getTipoActivo(), e.getNombreValor());
    }
}
