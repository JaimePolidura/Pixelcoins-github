package es.serversurvival.empresas.empresas.eventlisteners;

import es.bukkitclassmapper.commands.Command;
import es.jaime.EventListener;
import es.jaimetruman.annotations.Component;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;

@Component
public final class OnJugadorCambiadoNombre {
    private final EmpresasService empresasService;

    public OnJugadorCambiadoNombre() {
        this.empresasService = DependecyContainer.get(EmpresasService.class);
    }

    @EventListener
    public void on (JugadorCambiadoDeNombreEvento evento) {
        this.empresasService.getByOwner(evento.getAntiguoNombre()).forEach(empresa -> {
            this.empresasService.save(empresa.withOwner(evento.getNuevoNombre()));
        });
    }
}
