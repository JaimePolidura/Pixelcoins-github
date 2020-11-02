package es.serversurvival.mySQL;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;

import es.serversurvival.mySQL.tablasObjetos.Encantamiento;
import es.serversurvival.util.Funciones;
import es.serversurvival.mySQL.tablasObjetos.Jugador;

/**
 * II 240 -> 129
 */
public final class Jugadores extends MySQL {
    public static final Jugadores INSTANCE = new Jugadores();
    private Jugadores () {}

    public Jugador nuevoJugador(String nombre, double pixelcoin, int espacios, int nventas, double ingresos, double gastos, double beneficios, int ninpagos, int npagos) {
        executeUpdate("INSERT INTO jugadores (nombre, pixelcoin, espacios, nventas, ingresos, gastos, beneficios, ninpagos, npagos) VALUES ('" + nombre + "','" + pixelcoin + "','" + espacios + "','" + nventas + "','" + ingresos + "','" + gastos + "','" + beneficios + "','" + ninpagos + "','" + npagos + "')");

        return new Jugador(nombre, pixelcoin, espacios, nventas, ingresos, gastos, beneficios, ninpagos, npagos);
    }

    public Jugador getJugador(String jugador){
        ResultSet rs = executeQuery(String.format("SELECT * FROM jugadores WHERE nombre = '%s'", jugador));

        return (Jugador) buildSingleObjectFromResultSet(rs);
    }

    public boolean estaRegistrado (String nombreJugador) {
        return getJugador(nombreJugador) != null;
    }

    public void setPixelcoin(String nombre, double pixelcoin) {
        executeUpdate("UPDATE jugadores SET pixelcoin = '"+pixelcoin+"' WHERE nombre = '"+nombre+"'");
    }

    public void setNinpagos(String nombre, int ninpagos) {
        executeUpdate("UPDATE jugadores SET ninpagos = '"+ninpagos+"' WHERE nombre = '"+nombre+"'");
    }

    public void setNpagos(String nombre, int npagos) {
        executeUpdate("UPDATE jugadores SET npagos = '"+npagos+"' WHERE nombre = '"+nombre+"'");
    }

    public void setEspacios(String nombre, int espacios) {
        executeUpdate("UPDATE jugadores SET espacios = '"+espacios+"' WHERE nombre = '"+nombre+"'");
    }

    public void setEstadisticas(String nombre, double dinero, int nventas, double ingresos, double gastos) {
        executeUpdate("UPDATE jugadores SET pixelcoin = '"+dinero+"', nventas = '"+nventas+"', ingresos = '"+ingresos+"', gastos = '"+gastos+"', beneficios = '"+(ingresos - gastos)+"' WHERE nombre = '"+nombre+"'");
    }

    public void cambiarNombreJugador (String jugador, String nuevoNombreJugador) {
        executeUpdate("UPDATE jugadores SET nombre = '"+nuevoNombreJugador+"' WHERE nombre = '"+nuevoNombreJugador+"'");
    }

    public List<Jugador> getAllJugadores(){
        ResultSet rs = executeQuery("SELECT * FROM jugadores");

        return buildListFromResultSet(rs);
    }

    public List<Jugador> getTopRicos (){
        ResultSet rs = executeQuery("SELECT * FROM jugadores ORDER BY pixelcoin");

        return buildListFromResultSet(rs);
    }

    public List<Jugador> getTopPobres (){
        ResultSet rs = executeQuery("SELECT * FROM jugadores ORDER BY pixelcoin ASC");

        return buildListFromResultSet(rs);
    }

    public List<Jugador> getTopVendedores (){
        ResultSet rs = executeQuery("SELECT * FROM jugadores ORDER BY nventas DESC");

        return buildListFromResultSet(rs);
    }

    public List<Jugador> getTopFiables (){
        ResultSet rs = executeQuery("SELECT * FROM jugadores ORDER BY npagos DESC");

        return buildListFromResultSet(rs);
    }

    public List<Jugador> getTopMenosFiables (){
        ResultSet rs = executeQuery("SELECT * FROM jugadores ORDER BY ninpagos DESC");

        return buildListFromResultSet(rs);
    }

    public int getPosicionTopRicos (String player){
        Map<String, Double> topPlayers = Funciones.crearMapaTopPatrimonioPlayers(false);

        int pos = 1;
        for(Map.Entry<String, Double> entry: topPlayers.entrySet()){
            if(entry.getKey().equalsIgnoreCase(player))
                return pos;
            else
                pos++;
        }

        return -1;
    }

    public int getPosicionTopVendedores (String player){
        List<Jugador> jugadores = this.getAllJugadores();

        jugadores.sort((o1, o2) -> {
            Integer o1Int = o1.getNventas();
            Integer o2Int = o2.getNventas();

            return o2Int.compareTo(o1Int);
        });

        int pos = 1;
        for(Jugador jugador : jugadores){
            if(jugador.getNombre().equalsIgnoreCase(player)) return pos;
            pos++;
        }
        return -1;
    }

    @Override
    protected Jugador buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Jugador(
                rs.getString("nombre"),
                rs.getDouble("pixelcoin"),
                rs.getInt("espacios"),
                rs.getInt("nventas"),
                rs.getDouble("ingresos"),
                rs.getDouble("gastos"),
                rs.getDouble("beneficios"),
                rs.getInt("ninpagos"),
                rs.getInt("npagos")
        );
    }
}