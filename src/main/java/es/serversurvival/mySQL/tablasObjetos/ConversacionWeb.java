package es.serversurvival.mySQL.tablasObjetos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class ConversacionWeb implements TablaObjeto{
    @Getter private final Jugador web_nombre;
    @Getter private final Jugador server_nombre;
}
