package es.serversurvival.bolsa.activosinfo._shared.domain;

import es.serversurvival._shared.mysql.TablaObjeto;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class ActivoInfo implements TablaObjeto {
    @Getter private final String nombreActivo;
    @Getter private final double precio;
    @Getter private final SupportedTipoActivo tipoActivo;
    @Getter private final String nombreActivoLargo;

    public ActivoInfo withPrecio(double precio){
        return new ActivoInfo(nombreActivo, precio, tipoActivo, nombreActivoLargo);
    }

    public ActivoInfo withNombreActivoLargo(String nombreActivoLargo){
        return new ActivoInfo(nombreActivo, precio, tipoActivo, nombreActivoLargo);
    }

    public boolean esTipoAccion () {
        return this.getTipoActivo() == SupportedTipoActivo.ACCIONES;
    }

    public boolean esTipoCriptomoneda () {
        return this.getTipoActivo() == SupportedTipoActivo.CRIPTOMONEDAS;
    }

    public boolean esTipoMateriaPrima () {
        return this.getTipoActivo() == SupportedTipoActivo.MATERIAS_PRIMAS;
    }
}
