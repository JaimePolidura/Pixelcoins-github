package es.serversurvival.mensajes.eventlisteners;

import es.jaime.EventListener;
import es.jaimetruman.annotations.Component;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionesabiertas.dividendostask.DividendoPagadoEvento;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.mensajes._shared.application.MensajesService;

import java.text.DecimalFormat;

@Component
public final class OnDividendoPagado {
    private final DecimalFormat formatea = Funciones.FORMATEA;
    private final MensajesService mensajesService;

    public OnDividendoPagado(){
        this.mensajesService = DependecyContainer.get(MensajesService.class);
    }

    @EventListener
    public void onDividendoPagado (DividendoPagadoEvento evento) {
        mensajesService.save(evento.getJugador(), "Has cobrado " + evento.getPixelcoins() + " PC en dividendos por parte de la empresa " + evento.getTicker());
    }
}
