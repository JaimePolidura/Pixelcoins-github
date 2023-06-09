package es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain;

import es.serversurvival.pixelcoins.empresas.cambiardirector.CambiarDirectorVotacion;
import lombok.Getter;

public enum TipoVotacion {
    CAMBIAR_DIRECTOR(CambiarDirectorVotacion.class, "cambiar director");

    @Getter private final Class<? extends CambiarDirectorVotacion> votacionTypeClass;
    @Getter private final String nombre;

    TipoVotacion(Class<? extends CambiarDirectorVotacion> votacionTypeClass, String nombre) {
        this.votacionTypeClass = votacionTypeClass;
        this.nombre = nombre;
    }
}
