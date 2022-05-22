package es.serversurvival.bolsa.activosinfo._shared.domain;

import es.jaime.javaddd.domain.Aggregate;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public final class ActivoInfo extends Aggregate {
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
