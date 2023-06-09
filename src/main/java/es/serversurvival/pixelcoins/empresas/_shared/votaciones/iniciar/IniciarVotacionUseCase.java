package es.serversurvival.pixelcoins.empresas._shared.votaciones.iniciar;

import es.dependencyinjector.dependencies.annotations.UseCase;
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

    public UUID iniciar(Votacion votacion) {
        empresasValidador.empresaCotizada(votacion.getEmpresaId());
        empresasValidador.accionistaDeEmpresa(votacion.getEmpresaId(), votacion.getIniciadoPorJugadorId());
        empresasValidador.votacionNoRepetida(votacion);

        votacionesService.save(votacion);

        return votacion.getVotacionId();
    }
}
