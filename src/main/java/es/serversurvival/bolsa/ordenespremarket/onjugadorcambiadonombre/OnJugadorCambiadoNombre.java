package es.serversurvival.bolsa.ordenespremarket.onjugadorcambiadonombre;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.ordenespremarket._shared.application.OrdenesPremarketService;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

import java.text.DecimalFormat;

public final class OnJugadorCambiadoNombre {
    private final OrdenesPremarketService ordenesPremarketService;

    public OnJugadorCambiadoNombre(){
        this.ordenesPremarketService = DependecyContainer.get(OrdenesPremarketService.class);
    }

    @EventListener
    public void on (JugadorCambiadoDeNombreEvento evento) {
        this.ordenesPremarketService.findByJugador(evento.getAntiguoNombre()).forEach(ordenPremarket -> {
            this.ordenesPremarketService.save(ordenPremarket.withJugador(evento.getNuevoNombre()));
        });
    }
}
