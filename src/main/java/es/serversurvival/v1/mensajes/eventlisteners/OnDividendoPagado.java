package es.serversurvival.v1.mensajes.eventlisteners;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.v1.bolsa.posicionesabiertas.dividendostask.DividendoPagadoEvento;
import es.serversurvival.v1.mensajes._shared.application.MensajesService;
import lombok.AllArgsConstructor;

@EventHandler
@AllArgsConstructor
public final class OnDividendoPagado {
    private final MensajesService mensajesService;

    @EventListener
    public void onDividendoPagado (DividendoPagadoEvento evento) {
        mensajesService.save(evento.getJugador(), "Has cobrado " + evento.getPixelcoins() + " PC en dividendos por parte de la empresa " + evento.getTicker());
    }
}
