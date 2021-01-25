package es.serversurvival.mySQL.tablasObjetos;

import es.serversurvival.mySQL.enums.TipoPosicion;
import es.serversurvival.mySQL.enums.TipoValor;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public final class PosicionAbierta implements TablaObjeto {
    @Getter private final int id;
    @Getter private final String jugador;
    @Getter private final String tipo_activo;
    @Getter private final String nombre_activo;
    @Getter private final int cantidad;
    @Getter private final double precio_apertura;
    @Getter private final String fecha_apertura;
    @Getter private final String tipo_posicion;

    public boolean esLargo () {
        return this.getTipo_posicion().equalsIgnoreCase(TipoPosicion.LARGO.toString());
    }

    public boolean esCorto () {
        return this.getTipo_posicion().equalsIgnoreCase(TipoPosicion.CORTO.toString());
    }

    public boolean esTipoAccion () {
        return this.getTipo_activo().equalsIgnoreCase(TipoValor.ACCIONES.toString());
    }
}
