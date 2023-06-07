package es.serversurvival.v2.minecraftserver.empresas.votaciones.lorevotacionitem;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v2.pixelcoins.empresas.cambiardirector.CambiarDirectorVotacion;

import java.util.List;

@Service
public final class CambiarDirectorLoreBuilder implements VotacionItemLoreBuilder<CambiarDirectorVotacion> {
    @Override
    public List<String> build(CambiarDirectorVotacion votacion) {
        return null;
    }
}
