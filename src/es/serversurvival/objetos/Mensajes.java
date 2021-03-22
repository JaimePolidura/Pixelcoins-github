package es.serversurvival.objetos;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;

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
            String consulta = "SELECT * FROM mensajes";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("destinatario").equalsIgnoreCase(jugador)) {
                    mensajes.add(rs.getString("mensaje"));
                }
            }
        } catch (Exception e) {

        }
        return mensajes;
    }

    private void borrarMensajes(String jugador) {
        int id;
        try {
            String consulta = "SELECT * FROM mensajes";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("destinatario").equalsIgnoreCase(jugador)) {
                    id = rs.getInt("id_mensaje");
                    this.borrarMensaje(id);
                }
            }
        } catch (Exception e) {

        }
    }

    private boolean tieneMensajes(String jugador) {
        boolean tiene = false;
        try {
            String consulta = "SELECT * FROM mensajes";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("destinatario").equalsIgnoreCase(jugador)) {
                    tiene = true;
                    break;
                }
            }
        } catch (Exception e) {

        }
        return tiene;
    }

    public int getNMensajes(String jugador) {
        int nmensajes = 0;
        try {
            String consulta = "SELECT * FROM mensajes";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getString("destinatario").equalsIgnoreCase(jugador)) {
                    nmensajes = nmensajes + 1;
                }
            }
        } catch (Exception e) {

        }
        return nmensajes;
    }

    public void nuevoMensaje(String jugador, String mensaje) {
        try {
            conectar("root", "", "pixelcoins");
            String consulta = "INSERT INTO mensajes (destinatario, mensaje) VALUES ('" + jugador + "','" + mensaje + "')";
            Statement st = (Statement) conexion.createStatement();
            st.executeUpdate(consulta);
            desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mostrarMensajes(Player p) {
        String jugador = p.getName();
        boolean tiene = this.tieneMensajes(jugador);

        if (tiene == false) {
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