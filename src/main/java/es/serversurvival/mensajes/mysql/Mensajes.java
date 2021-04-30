package es.serversurvival.mensajes.mysql;

import es.serversurvival.shared.mysql.MySQL;
import org.bukkit.entity.Player;
import java.sql.*;
import java.util.List;

/**
 * 126 -> 50
 */
public final class Mensajes extends MySQL {
    public final static Mensajes INSTANCE = new Mensajes();
    private Mensajes () {}

    public void nuevoMensaje(String enviador, String destinatario, String mensaje) {
        executeUpdate("INSERT INTO mensajes (enviador, destinatario, mensaje) VALUES ('"+enviador+"','" + destinatario + "','" + mensaje + "')");
    }

    public List<Mensaje> getMensajesJugador(String destinatario){
        return buildListFromQuery(String.format("SELECT * FROM mensajes WHERE destinatario = '%s'", destinatario));
    }

    public void setDestinatario (String nombre, String nuevoNombre) {
        executeUpdate("UPDATE mensajes SET destinatario = '"+nuevoNombre+"' WHERE destinatario = '"+nombre+"'");
    }

    public void borrarMensajes(String jugador) {
        executeUpdate(String.format("DELETE FROM mensajes WHERE destinatario = '%s'", jugador));
    }

    @Override
    protected Mensaje buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Mensaje(rs.getInt("id"),
                rs.getString("enviador"),
                rs.getString("destinatario"),
                rs.getString("mensaje"));
    }
}
