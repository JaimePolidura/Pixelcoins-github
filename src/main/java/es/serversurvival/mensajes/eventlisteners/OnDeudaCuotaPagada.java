package es.serversurvival.mensajes.eventlisteners;

import es.jaime.EventListener;
import es.dependencyinjector.annotations.Component;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.deudas.pagarCuotas.DeudaCuotaPagadaEvento;
import es.serversurvival.mensajes._shared.application.MensajesService;

@Component
public final class OnDeudaCuotaPagada {
    private final MensajesService mensajesService;

    public OnDeudaCuotaPagada(){
        this.mensajesService = DependecyContainer.get(MensajesService.class);
    }

    @EventListener
    public void onDeudaCuotaPagada (DeudaCuotaPagadaEvento event) {
        if(event.getTiempoRestante() > 0){
            mensajesService.save(event.getDeudor(), "Has acabado de pagar la deuda con " + event.getAcredor());
            mensajesService.save(event.getAcredor(), event.getDeudor() + " ha acabado de pagar la deuda contigo");
        }else{
            mensajesService.save(event.getDeudor(), "Has pagado " + event.getPixelcoinsPagadas() + " PC por la deuda que tienes con " +
                    event.getAcredor() + " a " + event.getTiempoRestante() + " dias");
            mensajesService.save(event.getAcredor(), event.getDeudor() + " te ha pagado " + event.getPixelcoinsPagadas() +
                    " PC por la deuda que tiene a " + event.getTiempoRestante() + " dias contigo");
        }
    }
}
