package es.serversurvival.minecraftserver.empresas.votaciones.lorevotacionitem;

import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.Votacion;

import java.util.List;

public interface VotacionItemLoreBuilder<T extends Votacion> {
    List<String> build(T votacion);
}
