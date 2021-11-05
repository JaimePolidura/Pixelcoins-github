package es.serversurvival.mensajes.ver;

import es.serversurvival.mensajes._shared.mysql.Mensaje;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

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
