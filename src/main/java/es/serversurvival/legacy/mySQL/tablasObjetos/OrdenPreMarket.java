package es.serversurvival.legacy.mySQL.tablasObjetos;

import es.serversurvival.legacy.mySQL.enums.AccionOrden;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class OrdenPreMarket implements TablaObjeto{
    @Getter private final int id;
    @Getter private final String jugador;
    @Getter private final String nombre_activo;
    @Getter private final int cantidad;
    @Getter private final AccionOrden accion_orden;
}
