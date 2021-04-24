package es.serversurvival.nfs.mensajes.ver;

import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.nfs.mensajes.mysql.Mensaje;

import java.util.List;

public final class VerMensajesUseCase implements AllMySQLTablesInstances {
    public static final VerMensajesUseCase INSTANCE = new VerMensajesUseCase();

    private VerMensajesUseCase () {}

    public List<Mensaje> getMensajes(String jugador) {
        List<Mensaje> mensajes = mensajesMySQL.getMensajesJugador(jugador);

        mensajesMySQL.borrarMensajes(jugador);

        return mensajes;
    }
}
