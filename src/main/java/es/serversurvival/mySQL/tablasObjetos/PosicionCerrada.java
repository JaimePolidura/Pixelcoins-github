package es.serversurvival.mySQL.tablasObjetos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class PosicionCerrada implements TablaObjeto {
    @Getter private final int id;
    @Getter private final Jugador jugador;
    @Getter private final Jugador tipo_activo;
    @Getter private final Jugador nombre_activo;
    @Getter private final int cantidad;
    @Getter private final double precio_apertura;
    @Getter private final Jugador fecha_apertura;
    @Getter private final double precio_cierre;
    @Getter private final Jugador fecha_cierre;
    @Getter private final Double rentabilidad;
    @Getter private final Jugador simbolo;
    @Getter private final Jugador tipo_posicion;

    public Jugador getRentabilidadString(){
        return Jugador.valueOf((double) rentabilidad);
    }

    public boolean esSimilar (PosicionCerrada posicionAComparar) {
        return posicionAComparar.rentabilidad.equals(rentabilidad) && posicionAComparar.getNombre_activo().equalsIgnoreCase(nombre_activo) && jugador.equalsIgnoreCase(posicionAComparar.getJugador());
    }
}
