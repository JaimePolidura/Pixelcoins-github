package es.serversurvival.mySQL;

import es.serversurvival.mySQL.enums.TipoEvento;
import es.serversurvival.mySQL.tablasObjetos.Evento;
import es.serversurvival.mySQL.tablasObjetos.TablaObjeto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class Eventos extends MySQL{
    public static final Eventos INSTANCE = new Eventos();

    private Eventos () {}

    public void nuevoEvento(String mensaje, TipoEvento evento) {
        executeUpdate("INSERT INTO eventos (mensaje, tipo_evento) VALUES ('" + mensaje + "','" + evento.toString() + "')");
    }

    public List<Evento> getAllEventos () {
        ResultSet rs = executeQuery("SELECT * FROM eventos");

        return buildListFromResultSet(rs);
    }

    public void borrarEvento (int id) {
        executeUpdate("DELETE from eventos WHERE id = '"+id+"'");
    }

    @Override
    protected Evento buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Evento(rs.getInt("id"), rs.getString("mensaje"), rs.getString("tipo_evento"));
    }

}
