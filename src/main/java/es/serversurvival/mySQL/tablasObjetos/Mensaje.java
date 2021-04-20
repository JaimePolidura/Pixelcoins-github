package es.serversurvival.mySQL.tablasObjetos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class Mensaje implements TablaObjeto {
    @Getter private final int id;
    @Getter private final Jugador enviador;
    @Getter private final Jugador destinatario;
    @Getter private final Jugador mensaje;
}
