package es.serversurvival.minecraftserver.empresas.votaciones.lorevotacionitem;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.TipoVotacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.Votacion;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public final class VotacionItemLoreBuilderService {
    private final DependenciesRepository dependenciesRepository;

    public List<String> buildLore(Votacion votacion) {
        Class<? extends VotacionItemLoreBuilder> VotacionItemLoreBuilderClass = Arrays.stream(VotacionItemLoreBuilderMapper.values())
                .filter(mapping -> mapping.tipoVotacion == votacion.getTipo())
                .findFirst()
                .orElseThrow(() -> new ResourceNotFound("Tipo de votacion no registrada, hablar con Jaime"))
                .builderClass;

        VotacionItemLoreBuilder votacionItemLoreBuilder = dependenciesRepository.get(VotacionItemLoreBuilderClass);

        return votacionItemLoreBuilder.build(votacion);
    }

    private enum VotacionItemLoreBuilderMapper {
        ACEPTAR_OFERTA_COMPRA_EMPRESA(TipoVotacion.ACEPTAR_OFERTA_COMPRA_EMPRESA, PropuestaCompraEmpresaLoreBuilder.class),
        CAMBIAR_DIRECTOR(TipoVotacion.CAMBIAR_DIRECTOR, CambiarDirectorLoreBuilder.class);

        public final TipoVotacion tipoVotacion;
        public final Class<? extends VotacionItemLoreBuilder> builderClass;

        VotacionItemLoreBuilderMapper(TipoVotacion tipoVotacion, Class<? extends VotacionItemLoreBuilder> builderClass) {
            this.tipoVotacion = tipoVotacion;
            this.builderClass = builderClass;
        }
    }
}
