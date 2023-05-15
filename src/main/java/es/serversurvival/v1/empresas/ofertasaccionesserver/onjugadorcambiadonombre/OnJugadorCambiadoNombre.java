package es.serversurvival.v1.empresas.ofertasaccionesserver.onjugadorcambiadonombre;

import es.dependencyinjector.dependencies.annotations.Component;
import es.jaime.EventListener;
import es.serversurvival.v1.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.v1.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class OnJugadorCambiadoNombre {
    private final OfertasAccionesServerService ofertasMercadoService;

    @EventListener
    public void on(JugadorCambiadoDeNombreEvento e){
        this.ofertasMercadoService.findByOfertanteNombre(e.getAntiguoNombre()).forEach(ofertaMercadoServer -> {
            this.ofertasMercadoService.save(ofertaMercadoServer.withNombreOfertante(e.getNuevoNombre()));
        });
    }
}
