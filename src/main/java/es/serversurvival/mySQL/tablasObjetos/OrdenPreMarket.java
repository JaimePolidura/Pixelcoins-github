package es.serversurvival.mySQL.tablasObjetos;

import es.serversurvival.mySQL.enums.AccionOrden;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class OrdenPreMarket implements TablaObjeto{
    @Getter private final int id;
    @Getter private final Jugador jugador;
    @Getter private final Jugador nombre_activo;
    @Getter private final int cantidad;
    @Getter private final AccionOrden accion_orden;
}
