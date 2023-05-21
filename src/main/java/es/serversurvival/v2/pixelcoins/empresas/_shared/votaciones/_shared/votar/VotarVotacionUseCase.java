package es.serversurvival.v2.pixelcoins.empresas._shared.votaciones._shared.votar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.serversurvival.v2.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.AccionistasEmpresasService;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones._shared.votos.Voto;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones._shared.votos.VotosService;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones.ResultadoVotacionChecker;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@UseCase
@AllArgsConstructor
public final class VotarVotacionUseCase {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final ResultadoVotacionChecker resultadoVotacionChecker;
    private final EmpresasValidador empresasValidador;
    private final VotosService votosService;

    public void votar(VotarVotacionParametros parametros) {
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
