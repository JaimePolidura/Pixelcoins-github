package es.serversurvival.bolsa.posicionesabiertas.onjugadorcambiadonombre;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;

public final class OnJugadorCambiadoNombre {
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;

    public OnJugadorCambiadoNombre() {
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
    }

    @EventListener
    public void on(JugadorCambiadoDeNombreEvento evento){
        this.posicionesAbiertasSerivce.findByJugador(evento.getAntiguoNombre()).forEach(posicionAbierta -> {
            this.posicionesAbiertasSerivce.save(posicionAbierta.withJugador(evento.getNuevoNombre()));
        });
    }
}
