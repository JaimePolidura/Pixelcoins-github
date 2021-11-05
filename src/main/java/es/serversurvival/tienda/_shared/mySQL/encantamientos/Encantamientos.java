package es.serversurvival.tienda._shared.mySQL.encantamientos;

import es.jaimetruman.insert.Insert;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.MySQL;
import org.bukkit.enchantments.Enchantment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public final class  Encantamientos extends MySQL {
    public final static Encantamientos INSTANCE = new Encantamientos();
    private Encantamientos () {}

    public void nuevoEncantamiento(String encantamiento, int nivel, int id_oferta) {
        String query = Insert.table("encantamientos")
                .fields("encantamiento", "nivel", "id_oferta")
                .values(encantamiento, nivel, id_oferta);

        executeUpdate(query);
    }

    public List<Encantamiento> getEncantamientosOferta (int id_oferta){
        return buildListFromQuery(Select.from("encantamientos").where("id_oferta").equal(id_oferta));
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
        return new Encantamiento(rs.getInt("id"),
                rs.getString("encantamiento"),
                rs.getInt("nivel"),
                rs.getInt("id_oferta"));
    }
}
