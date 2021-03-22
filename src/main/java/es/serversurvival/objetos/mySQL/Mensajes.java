package es.serversurvival.objetos.mySQL;

import es.serversurvival.objetos.mySQL.tablasObjetos.Mensaje;
import javafx.scene.input.Mnemonic;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 126 -> 66
 */

public class Mensajes extends MySQL {

    private void borrarMensaje(int id) {
        executeUpdate(String.format("DELETE FROM mensajes WHERE id_mensaje = '%d'", id));
    }

    public List<Mensaje> getMensajesJugador(String destinatario){
        List<Mensaje> mensajes = new ArrayList<>();

        try{
            ResultSet rs = executeQuery(String.format("SELECT * FROM mensajes WHERE destinatario = '%s'", destinatario));
            while (rs.next()){
                mensajes.add(buildMensajeByResultset(rs));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return mensajes;
    }

    private void borrarMensajes(String jugador) {
        executeUpdate(String.format("DELETE FROM mensajes WHERE destinatario = '%s'", jugador));
    }

    public void nuevoMensaje(String jugador, String mensaje) {
        executeUpdate("INSERT INTO mensajes (destinatario, mensaje) VALUES ('" + jugador + "','" + mensaje + "')");
    }

    public void mostrarMensajes(Player p) {
        String jugador = p.getName();
        List<Mensaje> mensajes = getMensajesJugador(jugador);

        if (mensajes == null || mensajes.size() == 0) {
            p.sendMessage(ChatColor.DARK_RED + "No tienes ningun mensaje pendiente");
            return;
        }

        p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "   MENSAJES:");
        for (int i = 0; i < mensajes.size(); i++) {
            p.sendMessage(ChatColor.GOLD + "" + (i + 1) + " " + mensajes.get(i).getMensaje());
        }

        borrarMensajes(jugador);
    }

    private Mensaje buildMensajeByResultset (ResultSet rs) throws SQLException {
        return new Mensaje(rs.getInt("id_mensaje"),
                rs.getString("destinatario"),
                rs.getString("mensaje"));
    }
}