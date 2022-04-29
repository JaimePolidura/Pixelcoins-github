package es.serversurvival.webconnection.conversacionesweb.mysql;

import es.jaimetruman.delete.Delete;
import es.jaimetruman.delete.DeleteOptionsInitial;
import es.jaimetruman.insert.Insert;
import es.jaimetruman.select.Select;
import es.jaimetruman.select.SelectOptionInitial;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival.webconnection.ServerSocketWeb;
import es.serversurvival.webconnection.socketmessages.SocketMessagge;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ConversacionesWeb extends MySQLRepository {
    public static final ConversacionesWeb INSTANCE = new ConversacionesWeb();
    private final ServerSocketWeb socket = ServerSocketWeb.INSTANCE;

    private final DeleteOptionsInitial delete;
    private final SelectOptionInitial select;

    private ConversacionesWeb () {
        this.delete = Delete.from("conversacionesweb");
        this.select = Select.from("conversacionesweb");
    }

    public void nuevaConversacion (String web_nombre, String server_nombre) {
        String query = Insert.table("conversacionesweb")
                .fields("web_nombre", "server_nombre")
                .values(web_nombre.toLowerCase(), server_nombre.toLowerCase());

        executeUpdate(query);
    }

    public ConversacionWeb getConversacionServer (String server_nombre) {
        return (ConversacionWeb) buildObjectFromQuery(select.where("server_nombre").equal(server_nombre));
    }

    public boolean hayConversacionEntre (String web_nombre, String server_nombre) {
        return !isEmptyFromQuery(select.where("web_nombre").equal(web_nombre).and("server_nombre").equal(server_nombre));
    }

    public void borrarConversacionServer (String server_nombre) {
        executeUpdate(delete.where("server_nombre").equal(server_nombre));
    }

    public void borrarConversacionWeb (String web_nombre) {
        executeUpdate(delete.where("web_nombre").equal(web_nombre));
    }

    public void borrarTodasConversacionesWeb () {
        executeUpdate(delete);
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
