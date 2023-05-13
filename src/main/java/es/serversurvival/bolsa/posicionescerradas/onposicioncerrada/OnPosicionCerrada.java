package es.serversurvival.bolsa.posicionescerradas.onposicioncerrada;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.bolsa.posicionescerradas._shared.application.PosicionesCerradasService;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.PosicionCerradaEvento;
import lombok.AllArgsConstructor;

@EventHandler
@AllArgsConstructor
public final class OnPosicionCerrada {
    private final PosicionesCerradasService posicionesCerradasService;

    @EventListener
    public void onPosicionCerrada (PosicionCerradaEvento evento) {
        this.posicionesCerradasService.save(evento.buildPosicionCerrada());
    }
}
