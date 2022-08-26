package es.serversurvival.bolsa.posicionescerradas.onposicioncerrada;

import es.jaime.EventListener;
import es.jaimetruman.annotations.Component;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionescerradas._shared.application.PosicionesCerradasService;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.PosicionCerradaEvento;

@Component
public final class OnPosicionCerrada {
    private final PosicionesCerradasService posicionesCerradasService;

    public OnPosicionCerrada() {
        this.posicionesCerradasService = DependecyContainer.get(PosicionesCerradasService.class);
    }

    @EventListener
    public void onPosicionCerrada (PosicionCerradaEvento evento) {
        this.posicionesCerradasService.save(evento.buildPosicionCerrada());
    }
}
