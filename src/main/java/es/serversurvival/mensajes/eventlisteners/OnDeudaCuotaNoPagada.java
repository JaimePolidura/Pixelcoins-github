package es.serversurvival.mensajes.eventlisteners;

import es.jaime.EventListener;
import es.jaimetruman.annotations.Component;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.deudas.pagarCuotas.DeudaCuotaNoPagadaEvento;
import es.serversurvival.mensajes._shared.application.MensajesService;

@Component
public final class OnDeudaCuotaNoPagada {
    private final MensajesService mensajesService;

    public OnDeudaCuotaNoPagada(){
        this.mensajesService = DependecyContainer.get(MensajesService.class);
    }

    @EventListener
    private void onCuotaNoPagada (DeudaCuotaNoPagadaEvento evento) {
        this.mensajesService.save( evento.getAcredor(), evento.getDeudor() + " no te ha podido pagar ese dia la deuda por falta de pixelcoins");
        this.mensajesService.save( evento.getDeudor(), "no has podido pagar un dia la deuda con " + evento.getAcredor());
    }
}
