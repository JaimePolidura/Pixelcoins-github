package es.serversurvival.v2.pixelcoins.empresas._shared.votaciones.iniciar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.serversurvival.v2.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones.votaciones.Votacion;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones.votaciones.VotacionesService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class IniciarVotacionUseCase {
    private final VotacionesService votacionesService;
    private final EmpresasValidador empresasValidador;

    public void iniciar(Votacion votacion) {
        empresasValidador.empresaCotizada(votacion.getEmpresaId());
        empresasValidador.accionistaDeEmpresa(votacion.getEmpresaId(), votacion.getIniciadoPorJugadorId());
        empresasValidador.votacionNoRepetida(votacion);

        votacionesService.save(votacion);
    }
}
