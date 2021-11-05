package es.serversurvival.bolsa._shared.posicionesabiertas.mysql;

import es.serversurvival.bolsa._shared.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival.bolsa._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival.shared.mysql.TablaObjeto;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public final class PosicionAbierta implements TablaObjeto {
    @Getter private final int id;
    @Getter private final String jugador;
    @Getter private final TipoActivo tipo_activo;
    @Getter private final String nombre_activo;
    @Getter private final int cantidad;
    @Getter private final double precio_apertura;
    @Getter private final String fecha_apertura;
    @Getter private final TipoPosicion tipo_posicion;

    public boolean esLargo () {
        return tipo_posicion == TipoPosicion.LARGO;
    }

    public boolean esCorto () {
        return tipo_posicion == TipoPosicion.CORTO;
    }

    public boolean esTipoAccion () {
        return tipo_activo == TipoActivo.ACCIONES;
    }
       
    public boolean noEsTipoAccionServer () {
        return tipo_activo != TipoActivo.ACCIONES_SERVER;
    }

    public boolean esTipoAccionServer () {
        return tipo_activo == TipoActivo.ACCIONES_SERVER;
    }

    public boolean noEsTipoAccionServerYLargo () {
        return tipo_activo != TipoActivo.ACCIONES_SERVER && tipo_posicion == TipoPosicion.LARGO;
    }
}
