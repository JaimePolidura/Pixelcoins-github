package es.serversurvival.bolsa.posicionescerradas.onjugadorcambiadonombre;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionescerradas._shared.application.PosicionesCerradasService;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;

public final class OnJugadorCambiadoNombre {
    private final PosicionesCerradasService posicionesCerradasService;

    public OnJugadorCambiadoNombre() {
        this.posicionesCerradasService = DependecyContainer.get(PosicionesCerradasService.class);
    }

    @EventListener
    public void on (JugadorCambiadoDeNombreEvento evento) {
        posicionesCerradasService.findByJugador(evento.getAntiguoNombre()).forEach(posicionCerrada -> {
            this.posicionesCerradasService.save(posicionCerrada.withJugador(evento.getNuevoNombre()));
        });
    }
}
