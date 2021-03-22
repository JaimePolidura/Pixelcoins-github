package es.serversurvival.objetos;

import org.bukkit.enchantments.Enchantment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Encantamientos extends MySQL {

    private void borrarEncatamiento(int id_encantamiento) {
        try {
            String consulta2 = "DELETE FROM encantamientos WHERE id_encantamiento=\"" + id_encantamiento + "\"      ";
            Statement st2 = conexion.createStatement();
            st2.executeUpdate(consulta2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nuevoEncantamiento(String encantamiento, int nivel, int id_oferta) {
        try {
            String consulta2 = "INSERT INTO encantamientos (encantamiento, nivel, id_oferta) VALUES ('" + encantamiento + "','" + nivel + "','" + id_oferta + "')";
            Statement st2 = (Statement) conexion.createStatement();
            st2.executeUpdate(consulta2);
        } catch (Exception e) {

        }
    }

    public void borrarEncantamientosOferta(int id_oferta) {
        try {
            String consulta = "SELECT id_encantamiento FROM encantamientos WHERE id_oferta = ?";
            PreparedStatement pst = conexion.prepareStatement(consulta);
            pst.setInt(1, id_oferta);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                this.borrarEncatamiento(rs.getInt("id_encantamiento"));
            }
            rs.close();
        } catch (SQLException e) {

        }
    }

    public Map<Enchantment, Integer> getEncantamientosOferta(int id_oferta) {
        Map<Enchantment, Integer> enc = new HashMap<>();

        try {
            String consulta = "SELECT * FROM encantamientos WHERE id_oferta = ?";
            PreparedStatement pst = conexion.prepareStatement(consulta);
            pst.setInt(1, id_oferta);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                enc.put(Enchantment.getByName(rs.getString("encantamiento")), rs.getInt("nivel"));
            }
            rs.close();
        } catch (Exception e) {

        }
        return enc;
    }
}