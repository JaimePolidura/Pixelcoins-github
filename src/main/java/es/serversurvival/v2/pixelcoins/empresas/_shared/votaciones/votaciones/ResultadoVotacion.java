package es.serversurvival.v2.pixelcoins.empresas._shared.votaciones.votaciones;

public record ResultadoVotacion(boolean aceptado) {
    public static ResultadoVotacion fromEstadoVotacion(EstadoVotacion estadoVotacion) {
        return new ResultadoVotacion(estadoVotacion == EstadoVotacion.ACEPTADO);
    }
}
