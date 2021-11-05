package es.serversurvival.bolsa._shared.llamadasapi.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.shared.eventospixelcoins.PosicionAbiertaEvento;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class OnPosicionAbierta implements AllMySQLTablesInstances {
    @EventListener
    public void onOpenedPosition (PosicionAbiertaEvento e) {
        llamadasApiMySQL.nuevaLlamadaSiNoEstaReg(e.getTicker(), e.getPrecioUnidad(), e.getTipoActivo(), e.getNombreValor());
    }
}
