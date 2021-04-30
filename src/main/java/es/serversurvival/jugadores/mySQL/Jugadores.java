package es.serversurvival.jugadores.mySQL;

import java.sql.*;
import java.util.*;

import es.serversurvival.shared.mysql.MySQL;
import es.serversurvival.transacciones.mySQL.TipoTransaccion;
import es.serversurvival.utils.Funciones;
import org.bukkit.entity.Player;

/**
 * II 240 -> 129
 */
public final class Jugadores extends MySQL {
    public static final Jugadores INSTANCE = new Jugadores();
    private Jugadores () {}

    public void nuevoJugador(String nombre, double pixelcoins, int nventas, double ingresos, double gastos, int ninpagos, int npagos, String uuid) {
        int numero_cuenta = generearNumeroCuenta();

        executeUpdate("INSERT INTO jugadores (nombre, pixelcoins, nventas, ingresos, gastos, ninpagos, npagos, numero_cuenta, uuid) VALUES ('" + nombre + "','" + pixelcoins + "','" + nventas + "','" + ingresos + "','" + gastos + "','" + ninpagos + "','" + npagos + "', '"+numero_cuenta+"', '"+uuid+"')");
    }

    public int generearNumeroCuenta () {
        return (int) (Math.random() * 99999);
    }

    public boolean estaRegistradoNumeroCuentaPara (String jugador, int numero) {
        return !isEmptyFromQuery("SELECT * FROM jugadores WHERE numero_cuenta = '"+numero+"' AND nombre = '"+jugador+"'");
    }

    public Jugador getJugador(String jugador){
        return (Jugador) buildObjectFromQuery(String.format("SELECT * FROM jugadores WHERE nombre = '%s'", jugador));
    }

    public Jugador getJugadorUUID (String uuid){
        return (Jugador) buildObjectFromQuery("SELECT * FROM jugadores WHERE uuid = '"+uuid+"'");
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
        return buildListFromQuery("SELECT * FROM jugadores");
    }

    public List<Jugador> getTopVendedores (){
        return buildListFromQuery("SELECT * FROM jugadores ORDER BY nventas DESC");
    }

    public List<Jugador> getTopFiables (){
        return buildListFromQuery("SELECT * FROM jugadores ORDER BY npagos DESC");
    }

    public List<Jugador> getTopMenosFiables (){
        return buildListFromQuery("SELECT * FROM jugadores ORDER BY ninpagos DESC");
    }

    public void realizarTransferencia (String nombrePagador, String nombrePagado, double cantidad) {
        Jugador pagador = jugadoresMySQL.getJugador(nombrePagador);
        Jugador pagado = jugadoresMySQL.getJugador(nombrePagado);

        jugadoresMySQL.setPixelcoin(nombrePagado, pagado.getPixelcoins() + cantidad);
        jugadoresMySQL.setPixelcoin(nombrePagador, pagador.getPixelcoins() - cantidad);
    }

    public void realizarTransferenciaConEstadisticas (String nombrePagador, String nombrePagado, double cantidad) {
        Jugador pagador = jugadoresMySQL.getJugador(nombrePagador);
        Jugador pagado = jugadoresMySQL.getJugador(nombrePagado);

        jugadoresMySQL.setEstadisticas(nombrePagado, pagado.getPixelcoins() + cantidad, pagado.getNventas() + 1, pagado.getIngresos() + cantidad, pagado.getGastos());
        jugadoresMySQL.setEstadisticas(nombrePagador, pagador.getPixelcoins() - cantidad, pagador.getNventas(), pagador.getIngresos(), pagador.getGastos() + cantidad);
    }

    public Map<String, Jugador> getMapAllJugadores () {
        Map<String, Jugador> jugadoresMap = new HashMap<>();
        List<Jugador> jugadoresList = getAllJugadores();

        jugadoresList.forEach(jugador -> jugadoresMap.put(jugador.getNombre(), jugador));

        return jugadoresMap;
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

        jugadores.sort(Comparator.comparingInt(Jugador::getNventas));

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
