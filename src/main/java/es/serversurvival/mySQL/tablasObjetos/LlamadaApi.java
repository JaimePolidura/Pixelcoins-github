package es.serversurvival.mySQL.tablasObjetos;

import es.serversurvival.mySQL.enums.TipoValor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class LlamadaApi implements TablaObjeto {
    @Getter private final String simbolo;
    @Getter private final double precio;
    @Getter private final String tipo_activo;
    @Getter private final String nombre_activo;

    public boolean esTipoAccion () {
        return this.getTipo_activo().equalsIgnoreCase(TipoValor.ACCIONES.toString());
    }

    public boolean esTipoCriptomoneda () {
        return this.getTipo_activo().equalsIgnoreCase(TipoValor.CRIPTOMONEDAS.toString());
    }

    public boolean esTipoMateriaPrima () {
        return this.getTipo_activo().equalsIgnoreCase(TipoValor.MATERIAS_PRIMAS.toString());
    }
}
