package es.serversurvival.pixelcoins.empresas._shared.votaciones.iniciar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.Validador;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.Votacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.application.VotacionesService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@UseCase
@AllArgsConstructor
public final class IniciarVotacionUseCase {
    private final VotacionesService votacionesService;
    private final EmpresasValidador empresasValidador;
    private final Validador validador;
    private final EventBus eventBus;

    public UUID iniciar(Votacion votacion) {
        validador.stringLongitudEntre(votacion.getDescripcion(), 1, 16, "descripccion de la votacion");
        empresasValidador.empresaCotizada(votacion.getEmpresaId());
        empresasValidador.accionistaDeEmpresa(votacion.getEmpresaId(), votacion.getIniciadoPorJugadorId());
        empresasValidador.votacionNoRepetida(votacion);

        votacionesService.save(votacion);

        eventBus.publish(new VotacionIniciada(votacion.getVotacionId(), votacion.getIniciadoPorJugadorId(),
                votacion.getEmpresaId()));

        return votacion.getVotacionId();
    }
}
