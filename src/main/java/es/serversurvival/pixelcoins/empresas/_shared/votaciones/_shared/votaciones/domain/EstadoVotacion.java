package es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain;

import lombok.Getter;

public enum EstadoVotacion {
    ABIERTA(2, "Abierta"),
    EMPATE(1, "Empate"),
    RECHAZADO(1, "Rechazado"),
    ACEPTADO(1, "Aceptado");

    @Getter private final int showPriority;
    @Getter private final String nombre;

    EstadoVotacion(int showPriority, String nombre) {
        this.showPriority = showPriority;
        this.nombre = nombre;
    }
}
