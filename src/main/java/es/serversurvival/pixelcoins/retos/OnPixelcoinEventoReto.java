package es.serversurvival.pixelcoins.retos;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.RetoProgresivoService;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.Reto;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetosService;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import es.serversurvival.pixelcoins.retos._shared.retosadquiridos.application.AdquisitorRetos;
import es.serversurvival.pixelcoins.retos._shared.retosadquiridos.application.RetosAdquiridosService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@EventHandler
@AllArgsConstructor
public final class OnPixelcoinEventoReto {
    private final DependenciesRepository dependenciesRepository;
    private final RetosAdquiridosService retosAdquiridosService;
    private final AdquisitorRetos adquisitorRetos;
    private final RetosService retosService;

    @EventListener(pritority = Priority.LOWEST, value = InvocaAUnReto.class)
    public void on(InvocaAUnReto evento) {
        Map<UUID, List<RetoMapping>> retosByJugadorId = evento.retosByJugadorId();
        Object otro = evento.otroDatoReto();

        if(retosByJugadorId == null || retosByJugadorId.isEmpty()){
            return;
        }

        for (Map.Entry<UUID, List<RetoMapping>> entry : retosByJugadorId.entrySet()) {
            for (RetoMapping retoMapping : entry.getValue()) {
                Reto reto = retosService.getByMapping(retoMapping);
                UUID jugadorId = entry.getKey();

                intentarAdquirirReto(reto, jugadorId, retoMapping, otro);
            }
        }
    }

    private void intentarAdquirirReto(Reto reto, UUID jugadorId, RetoMapping retoMapping, Object otro) {
        if (retosAdquiridosService.estaAdquirido(jugadorId, reto.getRetoId())) {
            return;
        }

        if (reto.esTipoProgresivo()) {
            intentarAdquirirRetosProgresivos(retoMapping, reto, jugadorId, otro);
        }else{
            adquisitorRetos.adquirir(jugadorId, reto);
        }
    }

    private void intentarAdquirirRetosProgresivos(RetoMapping retoMapping, Reto reto, UUID jugadorId, Object otroId) {
        RetoProgresivoService retoProgresivoService = dependenciesRepository.get(retoMapping.getRetoProgresivoService());
        double cantidadActualJugador = retoProgresivoService.getCantidad(jugadorId, otroId);

        List<Reto> todosLosRectosEnLaProgresion = retosService.findByRetoPadreProgresionIdSortByPosicion(reto.getRetoPadreProgresionId());
        List<Reto> retosQuePuedeAdquirir = getRetosEnLaProgresionQuePuedeAdquirir(cantidadActualJugador, todosLosRectosEnLaProgresion, jugadorId);

        if (retosQuePuedeAdquirir.size() > 0) {
            adquisitorRetos.adquirir(jugadorId, retosQuePuedeAdquirir);
        }
    }

    private List<Reto> getRetosEnLaProgresionQuePuedeAdquirir(double cantidadActualJugador, List<Reto> todosRetosLinea, UUID jugadorId) {
        return todosRetosLinea.stream()
                .filter(reto -> cantidadActualJugador >= reto.getCantidadRequerida())
                .filter(reto -> !retosAdquiridosService.estaAdquirido(jugadorId, reto.getRetoId()))
                .collect(Collectors.toList());
    }
}
