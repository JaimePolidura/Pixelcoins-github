package es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.application;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.ResultadoVotacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.Votacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.VotacionFinalizadaListener;
import lombok.AllArgsConstructor;

import java.util.Optional;

@Service
@AllArgsConstructor
public final class VotacionFinalizadoCaller {
    private final DependenciesRepository dependencies;

    public void call(Votacion votacion, ResultadoVotacion resultado) {
        Optional<VotacionFinalizadaListener> votacionFinalizadaListener = dependencies.filterByImplementsInterfaceWithGeneric(
                VotacionFinalizadaListener.class,
                votacion.getTipo().getVotacionTypeClass()
        );

        if(votacionFinalizadaListener.isPresent()){
            votacionFinalizadaListener.get().on(votacion, resultado);
        }
    }
}
