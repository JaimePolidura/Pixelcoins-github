package es.serversurvival.bolsa.ordenespremarket.onjugadorcambiadonombre;

import es.dependencyinjector.dependencies.annotations.Component;
import es.jaime.EventListener;
import es.serversurvival.bolsa.ordenespremarket._shared.application.OrdenesPremarketService;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public final class OnJugadorCambiadoNombre {
    private final OrdenesPremarketService ordenesPremarketService;

    @EventListener
    public void on (JugadorCambiadoDeNombreEvento evento) {
        this.ordenesPremarketService.findByJugador(evento.getAntiguoNombre()).forEach(ordenPremarket -> {
            this.ordenesPremarketService.save(ordenPremarket.withJugador(evento.getNuevoNombre()));
        });
    }
}
