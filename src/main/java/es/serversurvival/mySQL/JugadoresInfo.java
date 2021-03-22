package es.serversurvival.mySQL;


import es.serversurvival.mySQL.tablasObjetos.Jugador;
import es.serversurvival.mySQL.tablasObjetos.JugadorInfo;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public final class JugadoresInfo extends MySQL {
    public final static JugadoresInfo INSTANCE = new JugadoresInfo();
    private JugadoresInfo () {}

    public void insertarNuevo (String nombre_jugador, String UUID) {
        executeUpdate("INSERT INTO jugadoresinfo (nombre_jugador, UUID) VALUES ('"+nombre_jugador+"', '"+UUID+"')");
    }

    public JugadorInfo getJugadorInfo(UUID uuid) {
        ResultSet rs = executeQuery("SELECT * FROM jugadoresinfo WHERE UUID = '"+uuid.toString()+"'");

        return (JugadorInfo) buildSingleObjectFromResultSet(rs);
    }

    public JugadorInfo getJugadorInfo(String nombre_jugador) {
        ResultSet rs = executeQuery("SELECT * FROM jugadoresinfo WHERE nombre_jugador = '"+nombre_jugador+"'");

        return (JugadorInfo) buildSingleObjectFromResultSet(rs);
    }

    public void setNombreJugador (String jugador, String nuevoNombre) {
        executeUpdate("UPDATE jugadoresinfo SET nombre_jugador = '"+nuevoNombre+"' WHERE nombre_jugador = '"+jugador+"'");
    }
    
    public void setUpJugado (Player player) {
        JugadorInfo jugadorInfo = getJugadorInfo(player.getUniqueId());
        String nombrePlayer = player.getName();

        if(jugadorInfo == null) {
            Jugador jugador = jugadoresMySQL.getJugador(player.getName());
            if(jugador == null)
                jugadoresMySQL.nuevoJugador(player.getName(), 0, 0, 0, 0, 0, 0, 0, 0);

            jugadoresInfoMySQL.insertarNuevo(player.getName(), player.getUniqueId().toString());
        }else if(!nombrePlayer.equalsIgnoreCase(jugadorInfo.getNombre_jugador())){
            transaccionesMySQL.cambiarNombreJugadorRegistros(jugadorInfo.getNombre_jugador(), nombrePlayer);
        }
    }

    @Override
    protected JugadorInfo buildObjectFromResultSet (ResultSet rs) throws SQLException {
        return new JugadorInfo(
                rs.getString("nombre_jugador"),
                rs.getString("UUID")
        );
    }
}