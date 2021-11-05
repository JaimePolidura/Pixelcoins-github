package es.serversurvival.mensajes.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.bolsa.posicionesabiertas.pagardividendos.DividendoPagadoEvento;
import es.serversurvival.mensajes.mysql.Mensajes;
import es.serversurvival.shared.utils.Funciones;

import java.text.DecimalFormat;

public final class OnDividendoPagado {
    private final DecimalFormat formatea = Funciones.FORMATEA;

    @EventListener
    public void onDividendoPagado (DividendoPagadoEvento evento) {
        Mensajes.INSTANCE.nuevoMensaje("",evento.getJugador(), "Has cobrado " + evento.getPixelcoins() + " PC en dividendos por parte de la empresa " + evento.getTicker());
    }
}
