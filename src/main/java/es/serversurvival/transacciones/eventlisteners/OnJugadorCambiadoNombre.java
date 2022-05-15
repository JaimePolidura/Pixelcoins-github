package es.serversurvival.transacciones.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;
import es.serversurvival.transacciones._shared.application.TransaccionesService;
import es.serversurvival.transacciones._shared.domain.Transaccion;

public final class OnJugadorCambiadoNombre {
    private final TransaccionesService transaccionesService;

    public OnJugadorCambiadoNombre(){
        this.transaccionesService = DependecyContainer.get(TransaccionesService.class);
    }

    @EventListener
    public void on(JugadorCambiadoDeNombreEvento evento) {
        this.transaccionesService.findByJugador(evento.getAntiguoNombre()).stream()
                .map(transaccion -> updateTransaccionConNuevoNombre(transaccion, evento.getNuevoNombre(), evento.getAntiguoNombre()))
                .forEach(this.transaccionesService::save);
    }

    private Transaccion updateTransaccionConNuevoNombre(Transaccion transaccion, String antiguoNombre, String nuevoNombre) {
        return transaccion.getComprador().equals(antiguoNombre) ?
                transaccion.withComprador(nuevoNombre) :
                transaccion.withVendedor(nuevoNombre);
    }
}
