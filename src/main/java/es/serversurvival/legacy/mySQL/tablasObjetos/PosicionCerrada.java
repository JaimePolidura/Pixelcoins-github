package es.serversurvival.legacy.mySQL.tablasObjetos;

import es.serversurvival.nfs.bolsa.llamadasapi.TipoActivo;
import es.serversurvival.legacy.mySQL.enums.TipoPosicion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class PosicionCerrada implements TablaObjeto {
    @Getter private final int id;
    @Getter private final String jugador;
    @Getter private final TipoActivo tipo_activo;
    @Getter private final String nombre_activo;
    @Getter private final int cantidad;
    @Getter private final double precio_apertura;
    @Getter private final String fecha_apertura;
    @Getter private final double precio_cierre;
    @Getter private final String fecha_cierre;
    @Getter private final Double rentabilidad;
    @Getter private final String simbolo;
    @Getter private final TipoPosicion tipo_posicion;

    public String getRentabilidadString(){
        return String.valueOf((double) rentabilidad);
    }

    public boolean esSimilar (PosicionCerrada posicionAComparar) {
        return posicionAComparar.rentabilidad.equals(rentabilidad) && posicionAComparar.getNombre_activo().equalsIgnoreCase(nombre_activo) && jugador.equalsIgnoreCase(posicionAComparar.getJugador());
    }
}
