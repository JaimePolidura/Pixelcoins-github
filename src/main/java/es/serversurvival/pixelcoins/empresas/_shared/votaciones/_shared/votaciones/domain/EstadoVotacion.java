package es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain;

import lombok.Getter;

public enum EstadoVotacion {
    ABIERTA(2),
    EMPATE(1),
    RECHAZADO(1),
    ACEPTADO(1);

    @Getter private final int showPriority;

    EstadoVotacion(int showPriority) {
        this.showPriority = showPriority;
    }
}
