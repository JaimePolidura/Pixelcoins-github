package es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain;

public record ResultadoVotacion(boolean aceptado) {
    public static ResultadoVotacion fromEstadoVotacion(EstadoVotacion estadoVotacion) {
        return new ResultadoVotacion(estadoVotacion == EstadoVotacion.ACEPTADO);
    }
}
