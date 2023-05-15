package es.serversurvival.v1.bolsa.posicionescerradas.onjugadorcambiadonombre;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.v1.bolsa.posicionescerradas._shared.application.PosicionesCerradasService;
import es.serversurvival.v1.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@EventHandler
public final class OnJugadorCambiadoNombre {
    private final PosicionesCerradasService posicionesCerradasService;

    @EventListener
    public void on (JugadorCambiadoDeNombreEvento evento) {
        posicionesCerradasService.findByJugador(evento.getAntiguoNombre()).forEach(posicionCerrada -> {
            this.posicionesCerradasService.save(posicionCerrada.withJugador(evento.getNuevoNombre()));
        });
    }
}
