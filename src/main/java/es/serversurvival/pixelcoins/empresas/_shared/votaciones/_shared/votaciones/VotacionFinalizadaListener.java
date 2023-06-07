package es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones;

public interface VotacionFinalizadaListener<T extends Votacion> {
    void on(T votacion, ResultadoVotacion resultado);
}
