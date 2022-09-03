package es.serversurvival.bolsa.posicionescerradas.onposicioncerrada;

import es.dependencyinjector.annotations.Component;
import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionescerradas._shared.application.PosicionesCerradasService;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.PosicionCerradaEvento;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public final class OnPosicionCerrada {
    private final PosicionesCerradasService posicionesCerradasService;

    @EventListener
    public void onPosicionCerrada (PosicionCerradaEvento evento) {
        this.posicionesCerradasService.save(evento.buildPosicionCerrada());
    }
}
