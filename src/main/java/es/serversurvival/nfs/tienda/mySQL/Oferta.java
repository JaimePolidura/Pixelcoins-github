package es.serversurvival.nfs.tienda.mySQL;

import es.serversurvival.legacy.mySQL.tablasObjetos.TablaObjeto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class Oferta implements TablaObjeto {
    @Getter private final int id;
    @Getter private final String jugador;
    @Getter private final String objeto;
    @Getter private final int cantidad;
    @Getter private final double precio;
    @Getter private final int durabilidad;
}
