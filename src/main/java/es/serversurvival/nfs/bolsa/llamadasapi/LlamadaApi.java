package es.serversurvival.nfs.bolsa.llamadasapi;

import es.serversurvival.legacy.mySQL.tablasObjetos.TablaObjeto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class LlamadaApi implements TablaObjeto {
    @Getter private final String simbolo;
    @Getter private final double precio;
    @Getter private final TipoActivo tipo_activo;
    @Getter private final String nombre_activo;

    public boolean esTipoAccion () {
        return this.getTipo_activo() == TipoActivo.ACCIONES;
    }

    public boolean esTipoCriptomoneda () {
        return this.getTipo_activo() == TipoActivo.CRIPTOMONEDAS;
    }

    public boolean esTipoMateriaPrima () {
        return this.getTipo_activo() == TipoActivo.MATERIAS_PRIMAS;
    }
}
