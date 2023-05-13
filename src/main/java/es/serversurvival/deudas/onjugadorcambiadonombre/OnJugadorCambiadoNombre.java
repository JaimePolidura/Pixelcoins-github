package es.serversurvival.deudas.onjugadorcambiadonombre;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.deudas._shared.application.DeudasService;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@EventHandler
public final class OnJugadorCambiadoNombre {
    private final DeudasService deudasService;

    @EventListener
    public void on (JugadorCambiadoDeNombreEvento evento) {
        this.deudasService.findByAcredor(evento.getAntiguoNombre()).forEach(deudaAcredor -> {
            this.deudasService.save(deudaAcredor.withAcredor(evento.getNuevoNombre()));
        });
        this.deudasService.findByDeudor(evento.getAntiguoNombre()).forEach(deudaDeudor -> {
            this.deudasService.save(deudaDeudor.withDeudor(evento.getNuevoNombre()));
        });
    }
}
