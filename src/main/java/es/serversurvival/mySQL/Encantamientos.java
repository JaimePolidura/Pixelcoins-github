package es.serversurvival.mySQL;

import es.serversurvival.mySQL.tablasObjetos.Encantamiento;
import org.bukkit.enchantments.Enchantment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Encantamientos extends MySQL {
    public final static Encantamientos INSTANCE = new Encantamientos();
    private Encantamientos () {}

    private void borrarEncatamiento(int id_encantamiento) {
        executeUpdate("DELETE FROM encantamientos WHERE id_encantamiento=\"" + id_encantamiento + "\"      ");
    }

    public void nuevoEncantamiento(String encantamiento, int nivel, int id_oferta) {
        executeUpdate("INSERT INTO encantamientos (encantamiento, nivel, id_oferta) VALUES ('" + encantamiento + "','" + nivel + "','" + id_oferta + "')");
    }

    public void borrarEncantamientosOferta(int id_oferta) {
        executeUpdate(String.format("DELETE FROM encantamientos WHERE id_oferta = '%d'", id_oferta));
    }

    public List<Encantamiento> getEncantamientosOferta (int id_oferta){
        ResultSet rs = executeQuery(String.format("SELECT * FROM encantamientos WHERE id_oferta = '%d'", id_oferta));

        return buildListFromResultSet(rs);
    }

    public void insertarEncantamientosDeItem (Map<Enchantment, Integer> enchantments, int id_oferta) {
        for(Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()){
            String encantamientoNombre = entry.getKey().getName();
            int nivel = entry.getValue();

            nuevoEncantamiento(encantamientoNombre, nivel, id_oferta);
        }
    }

    @Override
    protected Encantamiento buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Encantamiento(rs.getInt("id_encantamiento"),
                rs.getString("encantamiento"),
                rs.getInt("nivel"),
                rs.getInt("id_oferta"));
    }
}