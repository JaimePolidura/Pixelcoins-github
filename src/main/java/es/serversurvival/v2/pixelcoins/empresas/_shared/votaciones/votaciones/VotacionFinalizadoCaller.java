package es.serversurvival.v2.pixelcoins.empresas._shared.votaciones.votaciones;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Service;
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
