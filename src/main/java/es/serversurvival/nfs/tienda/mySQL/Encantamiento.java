package es.serversurvival.nfs.tienda.mySQL;

import es.serversurvival.legacy.mySQL.tablasObjetos.TablaObjeto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class Encantamiento implements TablaObjeto {
    @Getter private final int id;
    @Getter private final String encantamiento;
    @Getter private final int nivel;
    @Getter private final int id_oferta;
}
