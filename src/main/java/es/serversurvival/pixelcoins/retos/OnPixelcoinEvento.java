package es.serversurvival.pixelcoins.retos;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.jaime.Priority;
import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival.pixelcoins.retos._shared.RetoProgresivoService;
import es.serversurvival.pixelcoins.retos._shared.retos.Reto;
import es.serversurvival.pixelcoins.retos._shared.retos.RetosService;
import es.serversurvival.pixelcoins.retos._shared.retos.RetoMapping;
import es.serversurvival.pixelcoins.retos._shared.retos.TipoReto;
import es.serversurvival.pixelcoins.retos._shared.retosadquiridos.AdquisitorRetos;
import es.serversurvival.pixelcoins.retos._shared.retosadquiridos.RetosAdquiridosService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@EventHandler
@AllArgsConstructor
public final class OnPixelcoinEvento {
    private final DependenciesRepository dependenciesRepository;
    private final RetosAdquiridosService retosAdquiridosService;
    private final AdquisitorRetos adquisitorRetos;
    private final RetosService retosService;

    @EventListener(value = InvocaAUnReto.class, pritority = Priority.LOWEST)
    public void on(InvocaAUnReto evento) {
        Map<UUID, RetoMapping> retosByJugadorId = evento.retosByJugadorId();
        Object otroId = evento.otroDatoReto();

        if(retosByJugadorId == null || retosByJugadorId.isEmpty()){
            return;
        }

        for (Map.Entry<UUID, RetoMapping> entry : retosByJugadorId.entrySet()) {
            if (retosAdquiridosService.estaAdquirido(entry.getKey(), entry.getValue().getRetoId())) {
                continue;
            }

            RetoMapping retoMapping = entry.getValue();
            Reto reto = retosService.getById(retoMapping.getRetoId());
            UUID jugadorId = entry.getKey();

            if (reto.getTipo() == TipoReto.PROGRESIVO) {
                intentarAdquirirRetosProgresivos(retoMapping, reto, jugadorId, otroId);
            }else{
                adquisitorRetos.adquirir(jugadorId, retoMapping.getRetoId());
            }
        }
    }

    private void intentarAdquirirRetosProgresivos(RetoMapping retoMapping, Reto reto, UUID jugadorId, Object otroId) {
        RetoProgresivoService retoProgresivoService = dependenciesRepository.get(retoMapping.getRetoProgresivoService());
        double cantidadActualJugador = retoProgresivoService.getCantidad(jugadorId, otroId);

        List<Reto> todosLosRectosEnLaProgresion = retosService.findByRetoLineaPadre(reto.getRetoPadreProgresionId());
        List<Integer> retosIdQuePuedeAdquirir = getRetosEnLaProgresionQuePuedeAdquirir(cantidadActualJugador, todosLosRectosEnLaProgresion);

        if (retosIdQuePuedeAdquirir.size() > 0) {
            adquisitorRetos.adquirir(jugadorId, retosIdQuePuedeAdquirir);
        }
    }

    private List<Integer> getRetosEnLaProgresionQuePuedeAdquirir(double cantidadActualJugador, List<Reto> todosRetosLinea) {
        return todosRetosLinea.stream()
                .filter(reto -> cantidadActualJugador >= reto.getCantidadRequerida())
                .map(Reto::getRetoId)
                .collect(Collectors.toList());
    }
}
