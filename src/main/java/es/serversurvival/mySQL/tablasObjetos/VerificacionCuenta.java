package es.serversurvival.mySQL.tablasObjetos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class VerificacionCuenta implements TablaObjeto{
    @Getter private final Jugador jugador;
    @Getter private final int numero;
}
