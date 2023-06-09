package es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain;

import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.ResultadoVotacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.Votacion;

public interface VotacionFinalizadaListener<T extends Votacion> {
    void on(T votacion, ResultadoVotacion resultado);
}
