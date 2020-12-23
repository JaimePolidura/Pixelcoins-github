package es.serversurvival.mySQL;

import java.sql.*;
import java.util.*;

import es.serversurvival.util.Funciones;
import es.serversurvival.mySQL.tablasObjetos.Jugador;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * II 240 -> 129
 */
public final class Jugadores extends MySQL {
    public static final Jugadores INSTANCE = new Jugadores();
    private Jugadores () {}

    public Jugador nuevoJugador(String nombre, double pixelcoins, int nventas, double ingresos, double gastos, int ninpagos, int npagos, String uuid) {
        int numero_cuenta = generearNumeroCuenta();

        executeUpdate("INSERT INTO jugadores (nombre, pixelcoins, nventas, ingresos, gastos, ninpagos, npagos, numero_cuenta, uuid) VALUES ('" + nombre + "','" + pixelcoins + "','" + nventas + "','" + ingresos + "','" + gastos + "','" + ninpagos + "','" + npagos + "', '"+numero_cuenta+"', '"+uuid+"')");

        return new Jugador(nombre, pixelcoins, nventas, ingresos, gastos, ninpagos, npagos, numero_cuenta, uuid);
    }

    public int generearNumeroCuenta () {
        return (int) (Math.random() * 99999);
    }

    public boolean estaRegistradoNumeroCuentaPara (String jugador, int numero) {
        ResultSet rs = executeQuery("SELECT * FROM jugadores WHERE numero_cuenta = '"+numero+"' AND jugador = '"+jugador+"'");

        return !isEmpty(rs);
    }

    public boolean estaRegistradoNumeroCuenta (int numero) {
        ResultSet rs = executeQuery("SELECT * FROM jugadores WHERE numero_cuenta = '"+numero+"'");

        return !isEmpty(rs);
    }

    public Jugador getJugador(String jugador){
        ResultSet rs = executeQuery(String.format("SELECT * FROM jugadores WHERE nombre = '%s'", jugador));

        return (Jugador) buildSingleObjectFromResultSet(rs);
    }

    public Jugador getJugadorUUID (String uuid){
        ResultSet rs = executeQuery("SELECT * FROM jugadores WHERE uuid = '"+uuid+"'");

        return (Jugador) buildSingleObjectFromResultSet(rs);
    }

    public boolean estaRegistrado (String nombreJugador) {
        return getJugador(nombreJugador) != null;
    }

    public void setNumeroCuenta (String nombreJugador, int numero_cuenta) {
        executeUpdate("UPDATE jugadores SET numero_cuenta = '"+numero_cuenta+"' WHERE nombre = '"+nombreJugador+"'");
    }

    public void setPixelcoin(String nombre, double pixelcoin) {
        executeUpdate("UPDATE jugadores SET pixelcoins = '"+pixelcoin+"' WHERE nombre = '"+nombre+"'");
    }

    public void setNinpagos(String nombre, int ninpagos) {
        executeUpdate("UPDATE jugadores SET ninpagos = '"+ninpagos+"' WHERE nombre = '"+nombre+"'");
    }

    public void setNpagos(String nombre, int npagos) {
        executeUpdate("UPDATE jugadores SET npagos = '"+npagos+"' WHERE nombre = '"+nombre+"'");
    }

    public void setEstadisticas(String nombre, double dinero, int nventas, double ingresos, double gastos) {
        executeUpdate("UPDATE jugadores SET pixelcoins = '"+dinero+"', nventas = '"+nventas+"', ingresos = '"+ingresos+"', gastos = '"+gastos+"' WHERE nombre = '"+nombre+"'");
    }

    public void cambiarNombreJugador (String jugador, String nuevoNombreJugador) {
        executeUpdate("UPDATE jugadores SET nombre = '"+nuevoNombreJugador+"' WHERE nombre = '"+nuevoNombreJugador+"'");
    }

    public void setUuid (String jugador, String uuid){
        executeUpdate("UPDATE jugadores SET uuid = '"+uuid+"' WHERE nombre = '"+jugador+"'");
    }

    public List<Jugador> getAllJugadores(){
        ResultSet rs = executeQuery("SELECT * FROM jugadores");

        return buildListFromResultSet(rs);
    }

    public List<Jugador> getTopRicos (){
        ResultSet rs = executeQuery("SELECT * FROM jugadores ORDER BY pixelcoins");

        return buildListFromResultSet(rs);
    }

    public List<Jugador> getTopPobres (){
        ResultSet rs = executeQuery("SELECT * FROM jugadores ORDER BY pixelcoins ASC");

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

    public void setUpJugadorUnido (Player player) {
        Jugador jugadorPorUUID = jugadoresMySQL.getJugadorUUID(player.getUniqueId().toString());

        if(jugadorPorUUID == null){
            Jugador jugadorPorNombre = jugadoresMySQL.getJugador(player.getName());

            if(jugadorPorNombre == null){
                jugadoresMySQL.nuevoJugador(player.getName(), 0, 0, 0, 0, 0, 0, player.getUniqueId().toString());
            }else{
                jugadoresMySQL.setUuid(player.getName(), player.getUniqueId().toString());
            }
        }else{
            if(!player.getName().equalsIgnoreCase(jugadorPorUUID.getNombre())){
                transaccionesMySQL.cambiarNombreJugadorRegistros(jugadorPorUUID.getNombre(), player.getName());
            }

            if(jugadorPorUUID.getNumero_cuenta() == 0){
                jugadoresMySQL.setNumeroCuenta(player.getName(), jugadoresMySQL.generearNumeroCuenta());
            }
        }
    }

    @Override
    protected Jugador buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Jugador(
                rs.getString("nombre"),
                rs.getDouble("pixelcoins"),
                rs.getInt("nventas"),
                rs.getDouble("ingresos"),
                rs.getDouble("gastos"),
                rs.getInt("ninpagos"),
                rs.getInt("npagos"),
                rs.getInt("numero_cuenta"),
                rs.getString("uuid"));
    }
}
