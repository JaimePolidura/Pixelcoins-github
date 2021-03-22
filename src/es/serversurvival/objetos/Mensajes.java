package es.serversurvival.objetos;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private ArrayList<String> getMensajes(String jugador) {
        ArrayList<String> mensajes = new ArrayList<String>();

        try {
            String consulta = "SELECT mensaje FROM mensajes WHERE destinatario = ? ";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setString(1, jugador);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                mensajes.add(rs.getString("mensaje"));
            }
            rs.close();
        } catch (Exception e) {

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
        boolean tiene = this.tieneMensajes(jugador);

        if (!tiene) {
            p.sendMessage(ChatColor.DARK_RED + "No tienes ningun mensaje pendiente");
            return;
        }

        ArrayList<String> mensajes = this.getMensajes(p.getName());
        p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "   MENSAJES:");

        for (int i = 0; i < mensajes.size(); i++) {
            p.sendMessage(ChatColor.GOLD + "" + (i + 1) + " " + mensajes.get(i));
        }
        this.borrarMensajes(jugador);
    }
}