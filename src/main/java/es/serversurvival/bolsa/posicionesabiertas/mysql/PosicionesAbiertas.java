package es.serversurvival.bolsa.posicionesabiertas.mysql;

import es.serversurvival.bolsa.llamadasapi.mysql.LlamadaApi;
import es.serversurvival.bolsa.llamadasapi.mysql.TipoActivo;
import es.serversurvival.bolsa.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival.shared.mysql.MySQL;
import es.serversurvival.utils.apiHttp.IEXCloud_API;
import es.serversurvival.utils.Funciones;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.json.simple.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static es.serversurvival.utils.Funciones.*;

/**
 * 405 -> 246
 */
public final class PosicionesAbiertas extends MySQL {
    public final static PosicionesAbiertas INSTANCE = new PosicionesAbiertas();

    public static final double PORCENTAJE_CORTO = 5;

    private PosicionesAbiertas () {}

    public void nuevaPosicion(String jugador, TipoActivo tipoAcivo, String nombreActivo, int cantidad, double precioApertura, TipoPosicion tipoPosicion) {
        String fecha = dateFormater.format(new Date());

        executeUpdate("INSERT INTO posicionesabiertas (jugador, tipo_activo, nombre_activo, cantidad, precio_apertura, fecha_apertura, tipo_posicion) VALUES ('" + jugador + "','"+tipoAcivo.toString()+"','" + nombreActivo + "','" + cantidad + "','" + precioApertura + "', '" + fecha + "','"+tipoPosicion.toString()+"')");
    }

    public PosicionAbierta getPosicionAbierta(int id){
        return (PosicionAbierta) buildObjectFromQuery(String.format("SELECT * FROM posicionesabiertas WHERE id = '%d'", id));
    }

    public PosicionAbierta getPosicionAbierta (int id, String jugador) {
        return (PosicionAbierta) buildObjectFromQuery(String.format("SELECT * FROM posicionesabiertas WHERE id = '"+id+"' AND jugador = '%s'", jugador));
    }

    public PosicionAbierta getPosicionAbierta (int id, String jugador, TipoPosicion tipoPosicion) {
        return (PosicionAbierta) buildObjectFromQuery("SELECT * FROM posicionesabiertas WHERE id = '"+id+"' AND jugador = '"+jugador+"' AND tipo_posicion = '"+tipoPosicion.toString()+"'");
    }

    public List<PosicionAbierta> getPosicionesAbiertasJugador(String jugador){
        return buildListFromQuery(String.format("SELECT * FROM posicionesabiertas WHERE jugador = '%s'", jugador));
    }

    public List<PosicionAbierta> getPosicionesAbiertasJugadorCondicion (String jugador, Predicate<? super PosicionAbierta> condicion) {
        return getPosicionesAbiertasJugador(jugador).stream()
                .filter(condicion)
                .collect(Collectors.toList());
    }

    public List<PosicionAbierta> getTodasPosicionesAbiertas(){
        return buildListFromQuery("SELECT * FROM posicionesabiertas");
    }

    public List<PosicionAbierta> getTodasPosicionesAbiertasCondicion(Predicate<? super PosicionAbierta> condicion){
        return getTodasPosicionesAbiertas().stream()
                .filter(condicion)
                .collect(Collectors.toList());
    }

    public List<PosicionAbierta> getPosicionesAbiertasTipoActivo (String tipoActivo) {
        return buildListFromQuery(String.format("SELECT * FROM posicionesabiertas WHERE tipo_activo = '%s'", tipoActivo));
    }

    public void borrarPosicionAbierta(int id) {
        executeUpdate(String.format("DELETE FROM posicionesabiertas WHERE id = '%d'", id));
    }

    public boolean existeTicker(String nombre){
        return !isEmptyFromQuery(String.format("SELECT * FROM posicionesabiertas WHERE nombre_activo = '%s'", nombre));
    }

    public void setCantidad(int id, int cantidad) {
        executeUpdate(String.format("UPDATE posicionesabiertas SET cantidad = '%d' WHERE id = '%d'", cantidad, id));
    }

    public void setPrecioApertura (int id, double precio) {
        executeUpdate(String.format("UPDATE posicionesabiertas SET precio_apertura = '%d' WHERE id = '%d'", precio, id));
    }

    public void setJugador (String jugador, String nuevoJugador) {
        executeUpdate("UPDATE posicionesabiertas SET jugador = '"+nuevoJugador+"' WHERE jugador = '"+jugador+"'");
    }

    public List<PosicionAbierta> getPosicionAbierta (String owner, int cantidad, String ticker) {
        return buildListFromQuery("SELECT * FROM posicionesabiertas WHERE cantidad = '"+cantidad+"' AND jugador = '"+owner+"' AND nombre_activo = '"+ticker+"'");
    }

    public List<PosicionAbierta> getPosicionesAccionesServer(String empresa) {
        return buildListFromQuery("SELECT * FROM posicionesabiertas WHERE nombre_activo = '"+empresa+"' AND tipo_activo = 'ACCIONES_SERVER'");
    }

    public List<PosicionAbierta> getPosicionesServerJugador (String jugador) {
        return buildListFromQuery("SELECT * FROM posicionesabiertas WHERE jugador = '"+jugador+"' AND tipo_activo = 'ACCIONES_SERVER'");
    }

    public double getAllPixeloinsEnAcciones (String jugador) {
        List<PosicionAbierta> posLargas = getPosicionesAbiertasJugadorCondicion(jugador, PosicionAbierta::noEsTipoAccionServerYLargo);
        List<PosicionAbierta> posCortas = getPosicionesAbiertasJugadorCondicion(jugador, PosicionAbierta::esCorto);

        Map<String, LlamadaApi> llamadasApiMap = llamadasApiMySQL.getMapOfAllLlamadasApi();

        double pixelcoinsEnLargos = getSumaTotalListDouble(posLargas, pos -> llamadasApiMap.get(pos.getNombre_activo()).getPrecio() * pos.getCantidad());
        double pixelcoinsEnCortos = getSumaTotalListDouble(posCortas, pos -> (pos.getPrecio_apertura() - llamadasApiMap.get(pos.getNombre_activo()).getPrecio()) * pos.getCantidad());

        return pixelcoinsEnLargos + pixelcoinsEnCortos;
    }

    public Map<String, List<PosicionAbierta>> getAllPosicionesAbiertasMap (Predicate<? super PosicionAbierta> condition) {
        List<PosicionAbierta> posicionAbiertas = this.getTodasPosicionesAbiertas().stream()
                .filter(condition)
                .collect(Collectors.toList());

        return Funciones.mergeMapList(posicionAbiertas, PosicionAbierta::getJugador);
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

    public Map<PosicionAbierta, Integer> getPosicionesAbiertasConPesoJugador(String jugador, double totalInverito) {
        List<PosicionAbierta> posicionAbiertasJugador = getPosicionesAbiertasJugadorCondicion(jugador, PosicionAbierta::noEsTipoAccionServer);
        Map<PosicionAbierta, Integer> posicionesAbiertasConPeso = new HashMap<>();

        posicionAbiertasJugador.forEach( (posicion) -> {
            posicionesAbiertasConPeso.put(posicion, (int) rentabilidad(totalInverito, posicion.getCantidad() * llamadasApiMySQL.getLlamadaAPI(posicion.getNombre_activo()).getPrecio()));
        });

        return posicionesAbiertasConPeso;
    }


    @Override
    protected PosicionAbierta buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new PosicionAbierta(rs.getInt("id"),
                rs.getString("jugador"),
                TipoActivo.valueOf(rs.getString("tipo_activo")),
                rs.getString("nombre_activo"),
                rs.getInt("cantidad"),
                rs.getDouble("precio_apertura"),
                rs.getString("fecha_apertura"),
                TipoPosicion.valueOf(rs.getString("tipo_posicion")));
    }
}
