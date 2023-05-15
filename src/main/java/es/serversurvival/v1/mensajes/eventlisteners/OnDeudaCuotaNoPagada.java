package es.serversurvival.v1.mensajes.eventlisteners;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.v1.deudas.pagarCuotas.DeudaCuotaNoPagadaEvento;
import es.serversurvival.v1.mensajes._shared.application.MensajesService;
import lombok.AllArgsConstructor;

@EventHandler
@AllArgsConstructor
public final class OnDeudaCuotaNoPagada {
    private final MensajesService mensajesService;

    @EventListener
    private void onCuotaNoPagada (DeudaCuotaNoPagadaEvento evento) {
        this.mensajesService.save( evento.getAcredor(), evento.getDeudor() + " no te ha podido pagar ese dia la deuda por falta de pixelcoins");
        this.mensajesService.save( evento.getDeudor(), "no has podido pagar un dia la deuda con " + evento.getAcredor());
    }
}
