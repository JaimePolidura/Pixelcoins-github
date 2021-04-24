package es.serversurvival.legacy.mySQL;

import es.serversurvival.legacy.mySQL.tablasObjetos.ConversacionWeb;
import es.serversurvival.legacy.socketWeb.ServerSocketWeb;
import es.serversurvival.legacy.socketWeb.SocketMessagge;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ConversacionesWeb extends MySQL{
    public static final ConversacionesWeb INSTANCE = new ConversacionesWeb();
    private final ServerSocketWeb socket = ServerSocketWeb.INSTANCE;

    private ConversacionesWeb () {}

    public void nuevaConversacion (String web_nombre, String server_nombre) {
        executeUpdate("INSERT INTO conversacionesweb (web_nombre, server_nombre) VALUES ('"+web_nombre.toLowerCase()+"', '"+server_nombre.toLowerCase()+"')");
    }

    public ConversacionWeb getConversacionServer (String server_nombre) {
        return (ConversacionWeb) buildObjectFromQuery("SELECT * FROM conversacionesweb WHERE server_nombre = '"+server_nombre+"'");
    }

    public boolean hayConversacionEntre (String web_nombre, String server_nombre) {
        return !isEmptyFromQuery("SELECT * FROM conversacionesweb WHERE web_nombre = '"+web_nombre+"' AND server_nombre = '"+server_nombre+"'");
    }

    public void borrarConversacionServer (String server_nombre) {
        executeUpdate("DELETE FROM conversacionesweb WHERE server_nombre = '"+server_nombre+"'");
    }

    public void borrarConversacionWeb (String web_nombre) {
        executeUpdate("DELETE FROM conversacionesweb WHERE web_nombre = '"+web_nombre+"'");
    }

    public void borrarTodasConversacionesWeb () {
        executeUpdate("DELETE FROM conversacionesweb");
    }

    public void cerrarChat (ConversacionWeb conversacionWeb) {
        String message = "chatdisconnect-to=" + conversacionWeb.getWeb_nombre() + "&sender=" + conversacionWeb.getServer_nombre();
        this.borrarConversacionServer(conversacionWeb.getServer_nombre());
        socket.enviarMensaje(new SocketMessagge(message));
    }

    public void nuevoMensaje (ConversacionWeb conversacion, Player sender, String mensaje) {
        String mensajeSocket = "chat-sender=" + sender.getName() + "&to=" + conversacion.getWeb_nombre() + "&message=" + mensaje;
        socket.enviarMensaje(new SocketMessagge(mensajeSocket));

        sender.sendMessage("Tu> " + ChatColor.YELLOW + mensaje);
    }

    @Override
    protected ConversacionWeb buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new ConversacionWeb(rs.getString("web_nombre"), rs.getString("server_nombre"));
    }
}
