package es.serversurvival.legacy.mySQL.tablasObjetos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class ConversacionWeb implements TablaObjeto{
    @Getter private final String web_nombre;
    @Getter private final String server_nombre;
}
