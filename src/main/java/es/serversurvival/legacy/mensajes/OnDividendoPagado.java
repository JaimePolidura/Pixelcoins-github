package es.serversurvival.legacy.mensajes;

import es.jaime.EventListener;
import es.serversurvival.nfs.mensajes.mysql.Mensajes;
import es.serversurvival.legacy.mySQL.eventos.bolsa.DividendoPagadoEvento;
import es.serversurvival.legacy.util.Funciones;

import java.text.DecimalFormat;

public final class OnDividendoPagado {
    private final DecimalFormat formatea = Funciones.FORMATEA;

    @EventListener
    public void onDividendoPagado (DividendoPagadoEvento evento) {
        Mensajes.INSTANCE.nuevoMensaje("",evento.getJugador(), "Has cobrado " + evento.getPixelcoins() + " PC en dividendos por parte de la empresa " + evento.getTicker());
    }
}
