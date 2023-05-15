package es.serversurvival.v1.transacciones.eventlisteners;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.v1.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;
import es.serversurvival.v1.transacciones._shared.application.TransaccionesService;
import es.serversurvival.v1.transacciones._shared.domain.Transaccion;
import lombok.AllArgsConstructor;

@EventHandler
@AllArgsConstructor
public final class OnJugadorCambiadoNombre {
    private final TransaccionesService transaccionesService;

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
