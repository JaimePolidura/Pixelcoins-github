package es.serversurvival.empresas.empleados.onjugadorcambionnombre;

import es.dependencyinjector.dependencies.annotations.Component;
import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;

@Component
public final class OnJugadorCambiadoNombre {
    private final EmpleadosService empleadosService;

    public OnJugadorCambiadoNombre() {
        this.empleadosService = DependecyContainer.get(EmpleadosService.class);
    }

    @EventListener
    public void on (JugadorCambiadoDeNombreEvento evento) {
        this.empleadosService.findByJugador(evento.getAntiguoNombre()).forEach(empleado -> {
            this.empleadosService.save(empleado.withNombre(evento.getNuevoNombre()));
        });
    }
}
