package es.serversurvival.mySQL;

import es.serversurvival.menus.menus.Clickable;
import es.serversurvival.util.Funciones;
import es.serversurvival.mySQL.tablasObjetos.PosicionCerrada;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 331 -> 114
 * II: 114 -> 67
 */
public final class PosicionesCerradas extends MySQL {
    public final static PosicionesCerradas INSTANCE = new PosicionesCerradas();
    private PosicionesCerradas () {}

    public PosicionCerrada nuevaPosicion(String jugador, String tipo, String nombre,  int cantidad, double precioApertura, String fechaApertura, double precioCierre, String valorNombre, String tipoPosicion) {
        String fechaCierre = dateFormater.format(new Date());
        double rentabilidad = Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(precioApertura, precioCierre), 3);

        executeUpdate("INSERT INTO posicionescerradas (jugador, tipo_activo, simbolo, cantidad, precio_apertura, fecha_apertura, precio_cierre, fecha_cierre, rentabilidad, nombre_activo) VALUES ('" + jugador + "','"+tipo+"','" + nombre + "','" + cantidad + "','" + precioApertura + "', '" + fechaApertura + "','" + precioCierre + "','" + fechaCierre + "','" + rentabilidad + "','"+valorNombre+"')");
        return new PosicionCerrada(getMaxId(), jugador, tipo, nombre, cantidad, precioApertura, fechaApertura, precioCierre, fechaCierre, rentabilidad, valorNombre, tipoPosicion);
    }

    public PosicionCerrada nuevaPosicion(String jugador, String tipo, String nombre,  int cantidad, double precioApertura, String fechaApertura, double precioCierre, String valorNombre, String tipoPosicion, double rentabilidad) {
        String fechaCierre = dateFormater.format(new Date());

        executeUpdate("INSERT INTO posicionescerradas (jugador, tipo_activo, simbolo, cantidad, precio_apertura, fecha_apertura, precio_cierre, fecha_cierre, rentabilidad, nombre_activo) VALUES ('" + jugador + "','"+tipo+"','" + nombre + "','" + cantidad + "','" + precioApertura + "', '" + fechaApertura + "','" + precioCierre + "','" + fechaCierre + "','" + rentabilidad + "','"+valorNombre+"')");
        return new PosicionCerrada(getMaxId(), jugador, tipo, nombre, cantidad, precioApertura, fechaApertura, precioCierre, fechaCierre, rentabilidad, valorNombre, tipoPosicion);
    }

    public List<PosicionCerrada> getPosicionesCerradasTopRentabilidad(String jugador, int limite) {
        ResultSet rs = executeQuery(String.format("SELECT * FROM posicionescerradas WHERE jugador = '%s' ORDER BY rentabilidad DESC LIMIT %d", jugador, limite));

        return buildListFromResultSet(rs);
    }

    public List<PosicionCerrada> getPosicionesCerradasTopMenosRentabilidad(String jugador, int limite) {
        ResultSet rs = executeQuery(String.format("SELECT * FROM posicionescerradas WHERE jugador = '%s' ORDER BY rentabilidad ASC LIMIT %d", jugador, limite));

        return buildListFromResultSet(rs);
    }

    public List<PosicionCerrada> getTopRentabilidades(int limite) {
        ResultSet rs = executeQuery(String.format("SELECT * FROM posicionescerradas ORDER BY rentabilidad DESC LIMIT %d", limite));

        return buildListFromResultSet(rs);
    }

    public List<PosicionCerrada> getPeoresRentabilidades(int limite) {
        ResultSet rs = executeQuery(String.format("SELECT * FROM posicionescerradas ORDER BY rentabilidad ASC LIMIT %d", limite));

        return buildListFromResultSet(rs);
    }

    public List<PosicionCerrada> getTopRentabilidades() {
        ResultSet rs = executeQuery("SELECT * FROM posicionescerradas ORDER BY rentabilidad DESC");

        return buildListFromResultSet(rs);
    }

    public List<PosicionCerrada> getPeoresRentabilidades() {
        ResultSet rs = executeQuery("SELECT * FROM posicionescerradas ORDER BY rentabilidad ASC");

        return buildListFromResultSet(rs);
    }

    public List<PosicionCerrada> getPosicionesCerradasJugador(String name) {
        ResultSet rs = executeQuery(String.format("SELECT * FROM posicionescerradas WHERE jugador = '%s'", name));

        return buildListFromResultSet(rs);
    }

    public List<PosicionCerrada> getAllPosicionesCerradas () {
        ResultSet rs = executeQuery("SELECT * FROM posicionescerradas");
        
        return buildListFromResultSet(rs);
    }

    public void setJugador (String jugador, String nuevoJugador) {
        executeUpdate("UPDATE posicionescerradas SET jugador = '"+nuevoJugador+"' WHERE jugador = '"+jugador+"'");
    }

    private int getMaxId(){
        ResultSet rs = executeQuery("SELECT * FROM posicionescerradas ORDER BY id DESC LIMIT 1");
        PosicionCerrada posicionCerrada = (PosicionCerrada) buildSingleObjectFromResultSet(rs);

        return posicionCerrada != null ? posicionCerrada.getId() : -1;
    }

    @Override
    protected PosicionCerrada buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new PosicionCerrada(
                rs.getInt("id"),
                rs.getString("jugador"),
                rs.getString("tipo_activo"),
                rs.getString("simbolo"),
                rs.getInt("cantidad"),
                rs.getDouble("precio_apertura"),
                rs.getString("fecha_apertura"),
                rs.getDouble("precio_cierre"),
                rs.getString("fecha_cierre"),
                rs.getDouble("rentabilidad"),
                rs.getString("nombre_activo"),
                rs.getString("tipo_posicion")
        );
    }
}
