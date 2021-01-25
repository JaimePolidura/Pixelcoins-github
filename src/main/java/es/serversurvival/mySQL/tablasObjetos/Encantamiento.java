package es.serversurvival.mySQL.tablasObjetos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class Encantamiento implements TablaObjeto {
    @Getter private final int id;
    @Getter private final String encantamiento;
    @Getter private final int nivel;
    @Getter private final int id_oferta;
}
