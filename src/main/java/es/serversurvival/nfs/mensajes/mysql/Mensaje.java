package es.serversurvival.nfs.mensajes.mysql;

import es.serversurvival.legacy.mySQL.tablasObjetos.TablaObjeto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class Mensaje implements TablaObjeto {
    @Getter private final int id;
    @Getter private final String enviador;
    @Getter private final String destinatario;
    @Getter private final String mensaje;
}
