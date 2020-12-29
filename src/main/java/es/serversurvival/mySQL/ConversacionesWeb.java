package es.serversurvival.mySQL;

import es.serversurvival.mySQL.tablasObjetos.ConversacionWeb;
import es.serversurvival.mySQL.tablasObjetos.TablaObjeto;
import es.serversurvival.socketWeb.ServerSocketWeb;
import es.serversurvival.socketWeb.SocketMessagge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.Conversion;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConversacionesWeb extends MySQL{
    public static final ConversacionesWeb INSTANCE = new ConversacionesWeb();

    private ConversacionesWeb () {}

    public void nuevaConversacion (String web_nombre, String server_nombre) {
        executeUpdate("INSERT INTO conversacionesweb (web_nombre, server_nombre) VALUES ('"+web_nombre+"', '"+server_nombre+"')");
    }

    public ConversacionWeb getConversacionServer (String server_nombre) {
        ResultSet rs = executeQuery("SELECT * FROM conversacionesweb WHERE server_nombre = '"+server_nombre+"'");

        return (ConversacionWeb) buildSingleObjectFromResultSet(rs);
    }

    public boolean hayConversacionEntre (String web_nombre, String server_nombre) {
        ResultSet rs = executeQuery("SELECT * FROM conversacionesweb WHERE web_nombre = '"+web_nombre+"' AND server_nombre = '"+server_nombre+"'");
        ConversacionWeb conversacionWeb = (ConversacionWeb) buildSingleObjectFromResultSet(rs);

        return conversacionWeb != null;
    }

    public void borrarConversacionServer (String server_nombre) {
        executeUpdate("DELETE FROM conversacionesweb WHERE server_nombre = '"+server_nombre+"'");
    }

    public void borrarConversacionWeb (String web_nombre) {
        executeUpdate("DELETE FROM conversacionesweb WHERE web_nombre = '"+web_nombre+"'");
    }

    public void handlePrimerMensajeWeb (SocketMessagge socketMessagge) {
        String to = socketMessagge.get("to");
        String message = socketMessagge.get("message");
        String sender = socketMessagge.get("sender");
        Player toPlayer = Bukkit.getPlayer(to);

        boolean hayConversacinEntreSenderYTo = this.hayConversacionEntre(sender, to);

        System.out.println("---> " + hayConversacinEntreSenderYTo);

        if(!hayConversacinEntreSenderYTo){
            ConversacionWeb antiguaConversacionServer = getConversacionServer(to);

            if(antiguaConversacionServer != null){
                borrarConversacionServer(to);
                //TODO: Enviar mensaje a web didciendo que ha cerrado el chat
            }

            nuevaConversacion(sender, to);

            toPlayer.sendMessage(ChatColor.DARK_AQUA + "Te han enviado un mensaje desde la web. Para responder:  /re <mensaje>");
        }

        toPlayer.sendMessage(ChatColor.BOLD + "" + ChatColor.BOLD + sender + "> " + ChatColor.RESET + "" + ChatColor.YELLOW + message);
        toPlayer.playSound(toPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    public void nuevoMensaje (ConversacionWeb conversacion, Player sender, String mensaje) {
        String mensajeSocket = "chat-sender=" + sender.getName() + "&to=" + conversacion.getWeb_nombre() + "&message=" + mensaje;
        ServerSocketWeb.INSTANCE.enviarMensaje(mensajeSocket);

        sender.sendMessage("Tu> " + ChatColor.YELLOW + mensaje);
    }

    @Override
    protected ConversacionWeb buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new ConversacionWeb(rs.getString("web_nombre"), rs.getString("server_nombre"));
    }
}
