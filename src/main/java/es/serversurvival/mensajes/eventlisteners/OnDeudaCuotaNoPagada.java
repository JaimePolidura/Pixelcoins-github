package es.serversurvival.mensajes.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.deudas.pagarCuotas.DeudaCuotaNoPagadaEvento;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class OnDeudaCuotaNoPagada implements AllMySQLTablesInstances {
    @EventListener
    private void onCuotaNoPagada (DeudaCuotaNoPagadaEvento evento) {
        mensajesMySQL.nuevoMensaje("", evento.getAcredor(), evento.getDeudor() + " no te ha podido pagar ese dia la deuda por falta de pixelcoins");
        mensajesMySQL.nuevoMensaje("", evento.getDeudor(), "no has podido pagar un dia la deuda con " + evento.getAcredor());
    }
}
