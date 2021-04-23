package es.serversurvival.mensajes;

import es.jaime.EventListener;
import es.serversurvival.mySQL.Mensajes;
import es.serversurvival.mySQL.eventos.bolsa.DividendoPagadoEvento;
import es.serversurvival.util.Funciones;

import java.text.DecimalFormat;

public final class OnDividendoPagado {
    private final DecimalFormat formatea = Funciones.FORMATEA;

    @EventListener
    public void onDividendoPagado (DividendoPagadoEvento evento) {
        Mensajes.INSTANCE.nuevoMensaje("",evento.getJugador(), "Has cobrado " + evento.getPixelcoins() + " PC en dividendos por parte de la empresa " + evento.getTicker());
    }
}
