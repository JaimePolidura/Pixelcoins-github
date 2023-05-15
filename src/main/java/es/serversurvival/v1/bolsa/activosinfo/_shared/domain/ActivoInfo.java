package es.serversurvival.v1.bolsa.activosinfo._shared.domain;

import es.serversurvival.v1.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public final class ActivoInfo {
    @Getter private final String nombreActivo;
    @Getter private final double precio;
    @Getter private final TipoActivo tipoActivo;
    @Getter private final String nombreActivoLargo;

    public ActivoInfo withPrecio(double precio){
        return new ActivoInfo(nombreActivo, precio, tipoActivo, nombreActivoLargo);
    }

    public ActivoInfo withNombreActivoLargo(String nombreActivoLargo){
        return new ActivoInfo(nombreActivo, precio, tipoActivo, nombreActivoLargo);
    }

    public boolean esTipoAccion () {
        return this.getTipoActivo() == TipoActivo.ACCIONES;
    }

    public boolean esTipoCriptomoneda () {
        return this.getTipoActivo() == TipoActivo.CRIPTOMONEDAS;
    }

    public boolean esTipoMateriaPrima () {
        return this.getTipoActivo() == TipoActivo.MATERIAS_PRIMAS;
    }
}
