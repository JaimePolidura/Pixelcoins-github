package es.serversurvival.legacy.mySQL;

import es.jaime.EventListener;
import es.serversurvival.legacy.mySQL.enums.TipoActivo;
import es.serversurvival.legacy.mySQL.enums.TipoPosicion;
import es.serversurvival.legacy.mySQL.eventos.bolsa.PosicionCerradaEvento;
import es.serversurvival.legacy.mySQL.tablasObjetos.PosicionCerrada;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 331 -> 114
 * II: 114 -> 67
 */
public final class PosicionesCerradas extends MySQL {
    public final static PosicionesCerradas INSTANCE = new PosicionesCerradas();

    @EventListener
    public void onPosicionCerrada (PosicionCerradaEvento evento) {
        PosicionCerrada pos = evento.buildPosicionCerrada();

        nuevaPosicion(pos.getJugador(), pos.getTipo_activo(), pos.getSimbolo(), pos.getCantidad(), pos.getPrecio_apertura(), pos.getFecha_apertura(), pos.getPrecio_cierre(), pos.getNombre_activo(), pos.getRentabilidad(), pos.getTipo_posicion());
    }

    private void nuevaPosicion(String jugador, TipoActivo tipoActivo, String nombre, int cantidad, double precioApertura, String fechaApertura, double precioCierre, String valorNombre, double rentabilidad, TipoPosicion tipoPosicion) {
        String fechaCierre = dateFormater.format(new Date());

        executeUpdate("INSERT INTO posicionescerradas (jugador, tipo_activo, simbolo, cantidad, precio_apertura, fecha_apertura, precio_cierre, fecha_cierre, rentabilidad, nombre_activo, tipo_posicion) VALUES ('" + jugador + "','"+tipoActivo.toString()+"','" + nombre + "','" + cantidad + "','" + precioApertura + "', '" + fechaApertura + "','" + precioCierre + "','" + fechaCierre + "','" + rentabilidad + "','"+valorNombre+"', '"+tipoPosicion.toString()+"')");
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
                TipoActivo.valueOf(rs.getString("tipo_activo")),
                rs.getString("simbolo"),
                rs.getInt("cantidad"),
                rs.getDouble("precio_apertura"),
                rs.getString("fecha_apertura"),
                rs.getDouble("precio_cierre"),
                rs.getString("fecha_cierre"),
                rs.getDouble("rentabilidad"),
                rs.getString("nombre_activo"),
                TipoPosicion.valueOf(rs.getString("tipo_posicion"))
        );
    }
}
