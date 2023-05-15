package es.serversurvival.v1.empresas.empleados.onjugadorcambionnombre;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.v1.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.v1.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;
import lombok.AllArgsConstructor;

@EventHandler
@AllArgsConstructor
public final class OnJugadorCambiadoNombre {
    private final EmpleadosService empleadosService;

    @EventListener
    public void on (JugadorCambiadoDeNombreEvento evento) {
        this.empleadosService.findByJugador(evento.getAntiguoNombre()).forEach(empleado -> {
            this.empleadosService.save(empleado.withNombre(evento.getNuevoNombre()));
        });
    }
}
