package es.serversurvival.mySQL.tablasObjetos;

import es.serversurvival.mySQL.enums.TipoOperacion;
import es.serversurvival.mySQL.enums.TipoPosicion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class Orden implements TablaObjeto{
    @Getter private final int id;
    @Getter private final String jugador;
    @Getter private final String nombre_activo;
    @Getter private final int cantidad;
    @Getter private final TipoOperacion tipo_operacion;
    @Getter private final TipoPosicion tipo_posicion;
}
