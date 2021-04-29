package es.serversurvival.mensajes.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.deudas.pagarCuotas.DeudaCuotaPagadaEvento;

public final class OnDeudaCuotaPagada implements AllMySQLTablesInstances {
    @EventListener
    public void onDeudaCuotaPagada (DeudaCuotaPagadaEvento event) {
        if(event.getTiempoRestante() > 0){
            mensajesMySQL.nuevoMensaje("", event.getDeudor(), "Has acabado de pagar la deuda con " + event.getAcredor());
            mensajesMySQL.nuevoMensaje("", event.getAcredor(), event.getDeudor() + " ha acabado de pagar la deuda contigo");
        }else{
            mensajesMySQL.nuevoMensaje("", event.getDeudor(), "Has pagado " + event.getPixelcoinsPagadas() + " PC por la deuda que tienes con " +
                    event.getAcredor() + " a " + event.getTiempoRestante() + " dias");
            mensajesMySQL.nuevoMensaje("", event.getAcredor(), event.getDeudor() + " te ha pagado " + event.getPixelcoinsPagadas() +
                    " PC por la deuda que tiene a " + event.getTiempoRestante() + " dias contigo");
        }
    }
}
