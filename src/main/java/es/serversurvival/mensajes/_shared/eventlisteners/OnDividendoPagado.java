package es.serversurvival.mensajes._shared.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.bolsa._shared.posicionesabiertas.pagardividendos.DividendoPagadoEvento;
import es.serversurvival.mensajes._shared.mysql.Mensajes;
import es.serversurvival._shared.utils.Funciones;

import java.text.DecimalFormat;

public final class OnDividendoPagado {
    private final DecimalFormat formatea = Funciones.FORMATEA;

    @EventListener
    public void onDividendoPagado (DividendoPagadoEvento evento) {
        Mensajes.INSTANCE.nuevoMensaje("",evento.getJugador(), "Has cobrado " + evento.getPixelcoins() + " PC en dividendos por parte de la empresa " + evento.getTicker());
    }
}
