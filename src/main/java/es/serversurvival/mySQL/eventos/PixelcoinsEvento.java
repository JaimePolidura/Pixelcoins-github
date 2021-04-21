package es.serversurvival.mySQL.eventos;

import es.jaime.Event;
import es.serversurvival.util.Funciones;

/**
 * Evento base para el resto de eventos de todo el plugin
 */
public abstract class PixelcoinsEvento extends Event {
    public String formatFecha () {
        return getTimeOnCreated().format(Funciones.DATE_FORMATER);
    }
}
