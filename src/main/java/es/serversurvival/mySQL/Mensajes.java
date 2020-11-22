package es.serversurvival.mySQL;

import es.serversurvival.mySQL.tablasObjetos.Mensaje;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.sql.*;
import java.util.List;

/**
 * 126 -> 50
 */
public final class Mensajes extends MySQL {
    public final static Mensajes INSTANCE = new Mensajes();
    private Mensajes () {}

    private void borrarMensaje(int id) {
        executeUpdate(String.format("DELETE FROM mensajes WHERE id_mensaje = '%d'", id));
    }

    public List<Mensaje> getMensajesJugador(String destinatario){
        ResultSet rs = executeQuery(String.format("SELECT * FROM mensajes WHERE destinatario = '%s'", destinatario));

        return buildListFromResultSet(rs);
    }

    public void borrarMensajes(String jugador) {
        executeUpdate(String.format("DELETE FROM mensajes WHERE destinatario = '%s'", jugador));
    }

    public void nuevoMensaje(String enviador, String destinatario, String mensaje) {
        executeUpdate("INSERT INTO mensajes (enviador, destinatario, mensaje) VALUES ('"+enviador+"','" + destinatario + "','" + mensaje + "')");
    }

    public void setDestinatario (String nombre, String nuevoNombre) {
        executeUpdate("UPDATE mensajes SET destinatario = '"+nuevoNombre+"' WHERE destinatario = '"+nombre+"'");
    }

    public void mostrarMensajesYBorrar(Player player) {
        String jugador = player.getName();
        List<Mensaje> mensajes = getMensajesJugador(jugador);

        if (mensajes == null || mensajes.size() == 0) {
            player.sendMessage(ChatColor.DARK_RED + "No tienes ningun mensaje pendiente");
            return;
        }

        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "   MENSAJES:");
        for (int i = 0; i < mensajes.size(); i++) {
            player.sendMessage(ChatColor.GOLD + "" + (i + 1) + " " + mensajes.get(i).getMensaje());
        }
        borrarMensajes(jugador);
    }

    @Override
    protected Mensaje buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Mensaje(rs.getInt("id_mensaje"),
                rs.getString("enviador"),
                rs.getString("destinatario"),
                rs.getString("mensaje"));
    }
}
