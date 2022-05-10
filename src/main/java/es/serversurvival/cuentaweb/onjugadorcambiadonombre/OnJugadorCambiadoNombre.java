package es.serversurvival.cuentaweb.onjugadorcambiadonombre;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.cuentaweb._shared.application.CuentasWebService;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;

public final class OnJugadorCambiadoNombre {
    private final CuentasWebService cuentasWebService;

    public OnJugadorCambiadoNombre() {
        this.cuentasWebService = DependecyContainer.get(CuentasWebService.class);
    }

    @EventListener
    public void on (JugadorCambiadoDeNombreEvento evento) {
        var cuentaWeb = this.cuentasWebService.getByUsername(evento.getAntiguoNombre());

        this.cuentasWebService.save(cuentaWeb.withUsername(evento.getNuevoNombre()));
    }
}
