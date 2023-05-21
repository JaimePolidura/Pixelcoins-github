package es.serversurvival.v2.pixelcoins.empresas._shared.votaciones.votaciones;

import es.serversurvival.v2.pixelcoins.empresas.cambiardirector.CambiarDirectorVotacion;
import lombok.Getter;

public enum TipoVotacion {
    CAMBIAR_DIRECTOR(CambiarDirectorVotacion.class);

    @Getter private final Class<? extends CambiarDirectorVotacion> votacionTypeClass;

    TipoVotacion(Class<? extends CambiarDirectorVotacion> votacionTypeClass) {
        this.votacionTypeClass = votacionTypeClass;
    }
}
