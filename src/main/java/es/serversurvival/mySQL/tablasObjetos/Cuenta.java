package es.serversurvival.mySQL.tablasObjetos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class Cuenta implements TablaObjeto{
    @Getter private final int id;
    @Getter private final Jugador username;
    @Getter private final Jugador password;
    @Getter private final boolean active = true;
    @Getter private final Jugador roles = "USER";
}
