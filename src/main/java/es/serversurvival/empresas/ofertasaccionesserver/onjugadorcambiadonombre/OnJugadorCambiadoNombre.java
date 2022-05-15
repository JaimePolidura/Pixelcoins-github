package es.serversurvival.empresas.ofertasaccionesserver.onjugadorcambiadonombre;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.jugadores.setupjugadorunido.JugadorCambiadoDeNombreEvento;

public final class OnJugadorCambiadoNombre {
    private final OfertasAccionesServerService ofertasMercadoService;

    public OnJugadorCambiadoNombre() {
        this.ofertasMercadoService = DependecyContainer.get(OfertasAccionesServerService.class);
    }

    @EventListener
    public void on(JugadorCambiadoDeNombreEvento e){
        this.ofertasMercadoService.findByOfertanteNombre(e.getAntiguoNombre()).forEach(ofertaMercadoServer -> {
            this.ofertasMercadoService.save(ofertaMercadoServer.withNombreOfertante(e.getNuevoNombre()));
        });
    }
}
