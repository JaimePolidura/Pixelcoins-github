package es.serversurvival.jugadores._shared.mySQL;

import java.sql.*;
import java.util.*;

import es.jaimetruman.insert.Insert;
import es.jaimetruman.insert.InsertOptionFinal;
import es.jaimetruman.select.Select;
import es.jaimetruman.select.SelectOptionInitial;
import es.jaimetruman.update.Update;
import es.jaimetruman.update.UpdateOptionInitial;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.jugadores._shared.newformat.domain.Jugador;

import static es.jaimetruman.select.Order.*;

/**
 * II 240 -> 129
 */
public final class MySQLJugadoresRepository extends MySQLRepository {
    private final InsertOptionFinal insert;
    private final SelectOptionInitial select;
    private final UpdateOptionInitial update;

    public static final MySQLJugadoresRepository INSTANCE = new MySQLJugadoresRepository();

    private MySQLJugadoresRepository() {
        this.select = Select.from("jugadores");
        this.insert = Insert.table("jugadores").fields("nombre", "pixelcoins", "nventas", "ingresos", "gastos", "ninpagos", "npagos", "numero_cuenta", "uuid");
        this.update = Update.table("jugadores");
    }

    public void nuevoJugador(String nombre, double pixelcoins, int nventas, double ingresos, double gastos, int ninpagos, int npagos, String uuid) {
        int numero_cuenta = generearNumeroCuenta();

        executeUpdate(insert.values(nombre, pixelcoins, nventas, ingresos, gastos, ninpagos, npagos, numero_cuenta, uuid));
    }

    public int generearNumeroCuenta () {
        return (int) (Math.random() * 99999);
    }

    public boolean estaRegistradoNumeroCuentaPara (String jugador, int numero) {
        return !isEmptyFromQuery(select.where("numero_cuenta").equal(numero).and("nombre").equal(jugador));
    }

    public Jugador getJugador(String jugador){
        return (Jugador) buildObjectFromQuery(select.where("nombre").equal(jugador));
    }

    public Jugador getJugadorUUID (String uuid){
        return (Jugador) buildObjectFromQuery(select.where("uuid").equal(uuid));
    }

    public void setNumeroCuenta (String nombreJugador, int numero_cuenta) {
        executeUpdate(update.set("numero_cuenta", numero_cuenta).where("nombre").equal(nombreJugador));
    }

    public void setPixelcoin(String nombre, double pixelcoin) {
        executeUpdate(update.set("pixelcoins", pixelcoin).where("nombre").equal(nombre));
    }

    public void setNinpagos(String nombre, int ninpagos) {
        executeUpdate(update.set("ninpagos", ninpagos).where("nombre").equal(nombre));
    }

    public void setNpagos(String nombre, int npagos) {
        executeUpdate(update.set("npagos", npagos).where("nombre").equal(nombre));
    }

    public void setEstadisticas(String nombre, double dinero, int nventas, double ingresos, double gastos) {
        executeUpdate(update.set("pixelcoins", dinero).andSet("nventas", nventas).andSet("ingresos", ingresos).andSet("gastos", gastos).where("nombre").equal(nombre));
    }

    public void cambiarNombreJugador (String jugador, String nuevoNombreJugador) {
        executeUpdate(update.set("nombre", nuevoNombreJugador).where("nombre").equal(jugador));
    }

    public void setUuid (String jugador, String uuid){
        executeUpdate(update.set("uuid", uuid).where("nombre").equal(jugador));
    }

    public List<Jugador> getAllJugadores(){
        return buildListFromQuery(select);
    }

    public List<Jugador> getTopVendedores (){
        return buildListFromQuery(select.orderBy("nventas", DESC));
    }

    public List<Jugador> getTopFiables (){
        return buildListFromQuery(select.orderBy("npagos", DESC));
    }

    public List<Jugador> getTopMenosFiables (){
        return buildListFromQuery(select.orderBy("ninpagos", DESC));
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

        jugadoresMySQL.setEstadisticas(nombrePagado, pagado.getPixelcoins() + cantidad, pagado.getNVentas() + 1, pagado.getIngresos() + cantidad, pagado.getGastos());
        jugadoresMySQL.setEstadisticas(nombrePagador, pagador.getPixelcoins() - cantidad, pagador.getNVentas(), pagador.getIngresos(), pagador.getGastos() + cantidad);
    }

    public Map<String, Jugador> getMapAllJugadores () {
        Map<String, Jugador> jugadoresMap = new HashMap<>();
        List<Jugador> jugadoresList = getAllJugadores();

        jugadoresList.forEach(jugador -> jugadoresMap.put(jugador.getNombre(), jugador));

        return jugadoresMap;
    }

    @Override
    protected Jugador buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Jugador(
                UUID.fromString(rs.getString("nombre")),
                rs.getString("nombre"),
                rs.getDouble("pixelcoins"),
                rs.getInt("nVentas"),
                rs.getDouble("ingresos"),
                rs.getDouble("gastos"),
                rs.getInt("nInpagosDeuda"),
                rs.getInt("nPagosDeuda"),
                rs.getInt("numeroVerificacionCuenta")
        );
    }
}
