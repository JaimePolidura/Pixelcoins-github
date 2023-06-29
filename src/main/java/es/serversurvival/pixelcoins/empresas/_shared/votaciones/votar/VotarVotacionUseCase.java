package es.serversurvival.pixelcoins.empresas._shared.votaciones.votar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votos.domain.Voto;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votos.application.VotosService;
import es.serversurvival.pixelcoins.empresas._shared.votaciones.ResultadoVotacionChecker;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@UseCase
@AllArgsConstructor
public final class VotarVotacionUseCase implements UseCaseHandler<VotarVotacionParametros> {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final ResultadoVotacionChecker resultadoVotacionChecker;
    private final EmpresasValidador empresasValidador;
    private final VotosService votosService;
    
    @Override
    public void handle(VotarVotacionParametros parametros) {
        empresasValidador.accionistaDeEmpresa(parametros.getEmpresaId(), parametros.getJugadorId());
        empresasValidador.votacionPerteneceAEmpresa(parametros.getEmpresaId(), parametros.getVotacionId());
        empresasValidador.votacionAbierta(parametros.getVotacionId());
        empresasValidador.noHaVotado(parametros.getVotacionId(), parametros.getJugadorId());

        int numeroAccionesEnPropiedad = accionistasEmpresasService.getByEmpresaIdAndJugadorId(parametros.getEmpresaId(), parametros.getJugadorId())
                .getNAcciones();

        votosService.save(Voto.builder()
                .votoId(UUID.randomUUID())
                .votacionId(parametros.getVotacionId())
                .jugadorId(parametros.getJugadorId())
                .afavor(parametros.isAFavor())
                .nAcciones(numeroAccionesEnPropiedad)
                .fechaVotacion(LocalDateTime.now())
                .build());

        resultadoVotacionChecker.check(parametros.getVotacionId());
    }
}
