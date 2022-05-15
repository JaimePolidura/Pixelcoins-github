package es.serversurvival.deudas._shared.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.deudas._shared.newformat.application.DeudasService;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;

public final class OnJugadorCambiadoNombre {
    private final DeudasService deudasService;

    public OnJugadorCambiadoNombre(){
        this.deudasService = DependecyContainer.get(DeudasService.class);
    }

    @EventListener
    public void on (JugadorCambiadoDeNombreEvento evento) {
        this.deudasService.findDeudasByAcredor(evento.getAntiguoNombre()).forEach(deudaAcredor -> {
            this.deudasService.save(deudaAcredor.withAcredor(evento.getNuevoNombre()));
        });
        this.deudasService.findDeudasByDeudor(evento.getAntiguoNombre()).forEach(deudaDeudor -> {
            this.deudasService.save(deudaDeudor.withDeudor(evento.getNuevoNombre()));
        });
    }
}
