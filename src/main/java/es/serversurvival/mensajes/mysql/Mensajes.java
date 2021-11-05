package es.serversurvival.mensajes.mysql;

import es.jaimetruman.delete.Delete;
import es.jaimetruman.insert.Insert;
import es.jaimetruman.select.Select;
import es.jaimetruman.update.Update;
import es.serversurvival.shared.mysql.MySQL;
import org.bukkit.entity.Player;
import java.sql.*;
import java.util.List;

/**
 * 126 -> 50
 */
public final class Mensajes extends MySQL {
    public final static Mensajes INSTANCE = new Mensajes();
    private Mensajes () {}

    public void nuevoMensaje(String enviador, String destinatario, String mensaje) {
        String query = Insert.table("mensajes")
                .fields("enviador", "destinatario", "mensaje")
                .values(enviador, destinatario, mensaje);

        executeUpdate(query);
    }

    public List<Mensaje> getMensajesJugador(String destinatario){
        return buildListFromQuery(Select.from("mensajes").where("destinatario").equal(destinatario));
    }

    public void setDestinatario (String nombre, String nuevoNombre) {
        executeUpdate(Update.table("mensajes").set("destinatario", nuevoNombre).where("destinatario").equal(nombre));
    }

    public void borrarMensajes(String jugador) {
        executeUpdate(Delete.from("mensajes").where("destinatario").equal(jugador));
    }

    @Override
    protected Mensaje buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Mensaje(rs.getInt("id"),
                rs.getString("enviador"),
                rs.getString("destinatario"),
                rs.getString("mensaje"));
    }
}
