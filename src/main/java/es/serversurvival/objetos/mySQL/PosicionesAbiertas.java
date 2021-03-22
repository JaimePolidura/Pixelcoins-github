package es.serversurvival.objetos.mySQL;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.apiHttp.IEXCloud_API;
import es.serversurvival.objetos.mySQL.tablasObjetos.LlamadaApi;
import es.serversurvival.objetos.mySQL.tablasObjetos.PosicionAbierta;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 405 -> 246
 */
public class PosicionesAbiertas extends MySQL {
    private static String titulo = ChatColor.DARK_RED + "SELECCIONA Nº DE ACCIONES";
    private LlamadasApi llamadasApiMySQL = new LlamadasApi();
    private SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");


    public PosicionAbierta nuevaPosicion(String jugador, String tipo, String nombre, int cantidad, double precioApertura) {
        Date dt = new Date();
        String fecha = dateFormater.format(dt);

        executeUpdate("INSERT INTO posicionesabiertas (jugador, tipo, nombre, cantidad, precioApertura, fechaApertura) VALUES ('" + jugador + "','"+tipo+"','" + nombre + "','" + cantidad + "','" + precioApertura + "', '" + fecha + "')");
        return new PosicionAbierta(getMaxId(), jugador, TIPOS.ACCIONES.toString(), nombre, cantidad, precioApertura, fecha);
    }

    public PosicionAbierta getPosicionAbierta(int id){
        try{
            ResultSet rs = executeQuery(String.format("SELECT * FROM posicionesabiertas WHERE id = '%d'", id));
            while (rs.next()){
                return buildPosicionAbiertaByResultset(rs);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void borrarPosicionAbierta(int id) {
        executeUpdate(String.format("DELETE FROM posicionesabiertas WHERE id = '%d'", id));
    }

    public List<PosicionAbierta> getPosicionesAbiertasJugador(String jugador){
        List<PosicionAbierta> posicionAbiertas = new ArrayList<>();
        try{
            ResultSet rs = executeQuery(String.format("SELECT * FROM posicionesabiertas WHERE jugador = '%s'", jugador));
            while (rs.next()){
                posicionAbiertas.add(buildPosicionAbiertaByResultset(rs));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return posicionAbiertas;
    }

    public List<PosicionAbierta> getTodasPosicionesAbiertas(){
        List<PosicionAbierta> toReturn = new ArrayList<>();
        try{
            ResultSet rs = executeQuery("SELECT * FROM posicionesabiertas");
            while (rs.next()){
                toReturn.add(buildPosicionAbiertaByResultset(rs));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return toReturn;
    }

    public List<PosicionAbierta> getPosicionesAbiertasTipo (String tipo) {
        List<PosicionAbierta> posicionesAbiertas = new ArrayList<>();

        try{
            ResultSet rs = executeQuery(String.format("SELECT * FROM posicionesabiertas WHERE tipo = '%s'", tipo));
            while (rs.next()){
                posicionesAbiertas.add(buildPosicionAbiertaByResultset(rs));
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return posicionesAbiertas;
    }

    public boolean existeTicker(String nombre){
        try {
            ResultSet rs = executeQuery(String.format("SELECT * FROM posicionesabiertas WHERE nombre = '%s'", nombre));
            while (rs.next()){
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existe(int id) {
        return getPosicionAbierta(id) != null;
    }

    public void setCantidad(int id, int cantidad) {
        executeUpdate(String.format("UPDATE posicionesabiertas SET cantidad = '%d' WHERE id = '%d'", cantidad, id));
    }

    public void pagarDividendos() {
        Transacciones transaccionesMySQL = new Transacciones();
        List<PosicionAbierta> posicionAbiertas = getPosicionesAbiertasTipo("ACCIONES");
        Date fechaHoy = new Date();

        for(PosicionAbierta posicionAbierta : posicionAbiertas) {
            double dividendo;
            Date fechaPagoDividendos;

            try {
                JSONObject jsonDeLosDividendos = this.getJSONDividendos(posicionAbierta.getNombre());
                dividendo = getCantidadDePagoDeDividendoDesdeJSON(jsonDeLosDividendos);
                fechaPagoDividendos = getFechaPagoDividendosJSON(jsonDeLosDividendos);
            } catch (Exception e) {
                continue;
            }

            if (Funciones.diferenciaDias(fechaHoy, fechaPagoDividendos) == 0) {
                transaccionesMySQL.pagaDividendo(posicionAbierta.getNombre(), posicionAbierta.getJugador(), dividendo, posicionAbierta.getCantidad());
            }
        }
    }
    public static String getNombreSimbolo(String simbolo){
        String toReturn = "";

        switch (simbolo){
            case "BTCUSD":
                toReturn = "Bitcoin";
                break;
            case "LTCUSD":
                toReturn = "Litecoin";
                break;
            case "ETHUSD":
                toReturn = "Etherium";
                break;
            case "DJFUELUSGULF":
                toReturn = "Queroseno";
                break;
            case "DCOILBRENTEU":
                toReturn =  "Petroleo";
                break;
            case "DHHNGSP":
                toReturn =  "Gas natural";
                break;
            case "GASDESW":
                toReturn = "Diesel";
                break;
            default:
                toReturn = simbolo;
        }

        return toReturn;
    }

    public double getPrecioActual(String simbolo, String tipo) throws Exception {
        double precio = -1;

        switch (tipo.toUpperCase()){
            case "ACCIONES":
                precio = IEXCloud_API.getOnlyPrice(simbolo);
                break;
            case "CRIPTOMONEDAS":
                precio = IEXCloud_API.getPrecioCriptomoneda(simbolo);
                break;
            case "MATERIAS_PRIMAS":
                precio = IEXCloud_API.getPrecioMateriaPrima(simbolo);
                break;
        }
        return precio;
    }

    public double getPesoAccionEnCartera(int id){
        PosicionAbierta posicionAMedir = this.getPosicionAbierta(id);
        String jugador = posicionAMedir.getJugador();

        LlamadaApi precioEnLaAPI = llamadasApiMySQL.getLlamadaAPI(posicionAMedir.getNombre());

        double invertidoEnAccion = posicionAMedir.getCantidad() * precioEnLaAPI.getPrecio();

        List<PosicionAbierta> posicionesJugador = this.getPosicionesAbiertasJugador(jugador);
        double totalInvertido = posicionesJugador.stream()
                .mapToDouble((pos) -> pos.getCantidad() * llamadasApiMySQL.getLlamadaAPI(pos.getNombre()).getPrecio())
                .sum();

        return Funciones.rentabilidad(totalInvertido, invertidoEnAccion);
    }

    public Map<PosicionAbierta, Integer> getPosicionesAbiertasConPesoJugador(String jugador, double totalInverito) {
        List<PosicionAbierta> posicionAbiertasJugador = getPosicionesAbiertasJugador(jugador);
        Map<PosicionAbierta, Integer> posicionesAbiertasConPeso = new HashMap<>();

        posicionAbiertasJugador.forEach( (posicion) -> {
            posicionesAbiertasConPeso.put(posicion, (int) Funciones.rentabilidad(totalInverito, posicion.getCantidad() * llamadasApiMySQL.getLlamadaAPI(posicion.getNombre()).getPrecio()));
        });

        return posicionesAbiertasConPeso;
    }

    public double getTotalInvertido (String jugador) {
        List<PosicionAbierta> todasLasPosicionesJugador = getPosicionesAbiertasJugador(jugador);

        return todasLasPosicionesJugador.stream()
                .mapToDouble( (posicion) -> posicion.getCantidad() * llamadasApiMySQL.getLlamadaAPI(posicion.getNombre()).getPrecio())
                .sum();
    }

    private int getMaxId(){
        try{
            ResultSet rs = executeQuery("SELECT id FROM posicionesabiertas ORDER BY id DESC LIMIT 1");
            while (rs.next()){
                return rs.getInt("id");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return -1;
    }

    private double getCantidadDePagoDeDividendoDesdeJSON(JSONObject json) {
        return (double) json.get("amount");
    }

    private Date getFechaPagoDividendosJSON (JSONObject json) {
        return new Date((String) json.get("paymentDate"));
    }

    private JSONObject getJSONDividendos (String ticker) throws Exception {
        JSONArray jsonArray = IEXCloud_API.getDividendo(ticker, "week");
        return (JSONObject) jsonArray.get(0);
    }

    private PosicionAbierta buildPosicionAbiertaByResultset (ResultSet rs) throws SQLException {
        return new PosicionAbierta(rs.getInt("id"),
                rs.getString("jugador"),
                rs.getString("tipo"),
                rs.getString("nombre"),
                rs.getInt("cantidad"),
                rs.getDouble("precioApertura"),
                rs.getString("fechaApertura"));
    }

    public static enum TIPOS{
        ACCIONES,
        CRIPTOMONEDAS,
        MATERIAS_PRIMAS
    }
}