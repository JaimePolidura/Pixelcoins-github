package es.serversurvival.nfs.tienda.mySQL.encantamientos;

import es.serversurvival.nfs.shared.mysql.TablaObjeto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class Encantamiento implements TablaObjeto {
    @Getter private final int id;
    @Getter private final String encantamiento;
    @Getter private final int nivel;
    @Getter private final int id_oferta;
}
