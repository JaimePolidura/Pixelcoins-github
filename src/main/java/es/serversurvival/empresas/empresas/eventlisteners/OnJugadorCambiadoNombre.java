package es.serversurvival.empresas.empresas.eventlisteners;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;
import lombok.AllArgsConstructor;

@EventHandler
@AllArgsConstructor
public final class OnJugadorCambiadoNombre {
    private final EmpresasService empresasService;

    @EventListener
    public void on (JugadorCambiadoDeNombreEvento evento) {
        this.empresasService.getByOwner(evento.getAntiguoNombre()).forEach(empresa -> {
            this.empresasService.save(empresa.withOwner(evento.getNuevoNombre()));
        });
    }
}
