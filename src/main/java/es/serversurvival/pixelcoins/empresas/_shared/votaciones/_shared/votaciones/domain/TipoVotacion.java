package es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain;

import es.serversurvival.pixelcoins.empresas.cambiardirector.CambiarDirectorVotacion;
import es.serversurvival.pixelcoins.empresas.adquirir.proponer.AceptarOfertaCompraEmpresaVotacion;
import lombok.Getter;

public enum TipoVotacion {
    CAMBIAR_DIRECTOR(CambiarDirectorVotacion.class, "Cambiar director"),
    ACEPTAR_OFERTA_COMPRA_EMPRESA(AceptarOfertaCompraEmpresaVotacion.class, "Aceptar oferta compra empresa");

    @Getter private final Class<? extends Votacion> votacionTypeClass;
    @Getter private final String nombre;

    TipoVotacion(Class<? extends Votacion> votacionTypeClass, String nombre) {
        this.votacionTypeClass = votacionTypeClass;
        this.nombre = nombre;
    }
}
