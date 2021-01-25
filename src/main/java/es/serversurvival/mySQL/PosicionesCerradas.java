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

    public void nuevaPosicion(String jugador, String tipo, String nombre,  int cantidad, double precioApertura, String fechaApertura, double precioCierre, String valorNombre, String tipoPosicion) {
        String fechaCierre = dateFormater.format(new Date());
        double rentabilidad = Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(precioApertura, precioCierre), 3);

        executeUpdate("INSERT INTO posicionescerradas (jugador, tipo_activo, simbolo, cantidad, precio_apertura, fecha_apertura, precio_cierre, fecha_cierre, rentabilidad, nombre_activo) VALUES ('" + jugador + "','"+tipo+"','" + nombre + "','" + cantidad + "','" + precioApertura + "', '" + fechaApertura + "','" + precioCierre + "','" + fechaCierre + "','" + rentabilidad + "','"+valorNombre+"')");
    }

    public void nuevaPosicion(String jugador, String tipo, String nombre,  int cantidad, double precioApertura, String fechaApertura, double precioCierre, String valorNombre, String tipoPosicion, double rentabilidad) {
        String fechaCierre = dateFormater.format(new Date());

        executeUpdate("INSERT INTO posicionescerradas (jugador, tipo_activo, simbolo, cantidad, precio_apertura, fecha_apertura, precio_cierre, fecha_cierre, rentabilidad, nombre_activo) VALUES ('" + jugador + "','"+tipo+"','" + nombre + "','" + cantidad + "','" + precioApertura + "', '" + fechaApertura + "','" + precioCierre + "','" + fechaCierre + "','" + rentabilidad + "','"+valorNombre+"')");
    }

    public List<PosicionCerrada> getPosicionesCerradasTopRentabilidad(String jugador, int limite) {
        return buildListFromQuery(String.format("SELECT * FROM posicionescerradas WHERE jugador = '%s' ORDER BY rentabilidad DESC LIMIT %d", jugador, limite));
    }

    public List<PosicionCerrada> getPosicionesCerradasTopMenosRentabilidad(String jugador, int limite) {
        return buildListFromQuery(String.format("SELECT * FROM posicionescerradas WHERE jugador = '%s' ORDER BY rentabilidad ASC LIMIT %d", jugador, limite));
    }

    public List<PosicionCerrada> getTopRentabilidades() {
        return buildListFromQuery("SELECT * FROM posicionescerradas ORDER BY rentabilidad DESC");
    }

    public List<PosicionCerrada> getPeoresRentabilidades() {
        return buildListFromQuery("SELECT * FROM posicionescerradas ORDER BY rentabilidad ASC");
    }

    public List<PosicionCerrada> getPosicionesCerradasJugador(String name) {
        return buildListFromQuery(String.format("SELECT * FROM posicionescerradas WHERE jugador = '%s'", name));
    }

    public void setJugador (String jugador, String nuevoJugador) {
        executeUpdate("UPDATE posicionescerradas SET jugador = '"+nuevoJugador+"' WHERE jugador = '"+jugador+"'");
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
