package es.serversurvival.bolsa.posicionesabiertas.onjugadorcambiadonombre;

import es.dependencyinjector.dependencies.annotations.Component;
import es.jaime.EventListener;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public final class OnJugadorCambiadoNombre {
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;

    @EventListener
    public void on(JugadorCambiadoDeNombreEvento evento){
        this.posicionesAbiertasSerivce.findByJugador(evento.getAntiguoNombre()).forEach(posicionAbierta -> {
            this.posicionesAbiertasSerivce.save(posicionAbierta.withJugador(evento.getNuevoNombre()));
        });
    }
}
