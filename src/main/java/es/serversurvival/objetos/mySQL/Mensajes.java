package es.serversurvival.objetos.mySQL;

import es.serversurvival.objetos.mySQL.tablasObjetos.Mensaje;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Mensajes extends MySQL {

    private void borrarMensaje(int id) {
        try {
            String consulta = "DELETE FROM mensajes WHERE id_mensaje= ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (Exception e) {

        }
    }

    public List<Mensaje> getMensajesJugador(String destinatario){
        List<Mensaje> mensajes = new ArrayList<>();

        try{
            String consulta = "SELECT * FROM mensajes WHERE destinatario = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, destinatario);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                mensajes.add(new Mensaje(
                        rs.getInt("id_mensaje"),
                        rs.getString("destinatario"),
                        rs.getString("mensaje")
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return mensajes;
    }

    private void borrarMensajes(String jugador) {
        try {
            String consulta = "SELECT id_mensaje FROM mensajes WHERE destinatario = ? ";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, jugador);
            ResultSet rs = pst.executeQuery();

            int id_mensajeActual = 0;
            while (rs.next()) {
                id_mensajeActual = rs.getInt("id_mensaje");
                this.borrarMensaje(id_mensajeActual);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean tieneMensajes(String jugador) {
        boolean tieneMensajes = false;
        try {
            String consulta = "SELECT mensaje FROM mensajes WHERE destinatario = ? ";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, jugador);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                tieneMensajes = true;
            }
            rs.close();
        } catch (Exception e) {

        }
        return tieneMensajes;
    }

    public int getNMensajes(String jugador) {
        int nmensajes = 0;
        try {
            String consulta = "SELECT mensaje FROM mensajes WHERE destinatario = ? ";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, jugador);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                nmensajes = nmensajes + 1;
            }
            rs.close();
        } catch (Exception e) {

        }
        return nmensajes;
    }

    public void nuevoMensaje(String jugador, String mensaje) {
        try {
            String consulta = "INSERT INTO mensajes (destinatario, mensaje) VALUES ('" + jugador + "','" + mensaje + "')";
            Statement st = (Statement) conexion.createStatement();
            st.executeUpdate(consulta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mostrarMensajes(Player p) {
        String jugador = p.getName();
        List<Mensaje> mensajes = this.getMensajesJugador(jugador);

        if (mensajes == null || mensajes != null) {
            p.sendMessage(ChatColor.DARK_RED + "No tienes ningun mensaje pendiente");
            return;
        }

        p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "   MENSAJES:");

        for (int i = 0; i < mensajes.size(); i++) {
            p.sendMessage(ChatColor.GOLD + "" + (i + 1) + " " + mensajes.get(i).getMensaje());
        }
        this.borrarMensajes(jugador);
    }
}