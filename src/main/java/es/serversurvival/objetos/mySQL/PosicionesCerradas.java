package es.serversurvival.objetos.mySQL;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.tablasObjetos.PosicionCerrada;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 331 -> 114
 */

public class PosicionesCerradas extends MySQL {
    private SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");

    public PosicionCerrada nuevaPosicion(String jugador, String tipo, String nombre,  int cantidad, double precioApertura, String fechaApertura, double precioCierre) {
        Date fechaHoy = new Date();
        String fechaCierre = dateFormater.format(fechaHoy);
        double rentabilidad = Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(precioApertura, precioCierre), 3);

        executeUpdate("INSERT INTO posicionescerradas (jugador, tipo, nombre, cantidad, precioApertura, fechaApertura, precioCierre, fechaCierre, rentabilidad) VALUES ('" + jugador + "','"+tipo+"','" + nombre + "','" + cantidad + "','" + precioApertura + "', '" + fechaApertura + "','" + precioCierre + "','" + fechaCierre + "','" + rentabilidad + "')");
        return new PosicionCerrada(getMaxId(), jugador, tipo, nombre, cantidad, precioApertura, fechaApertura, precioCierre, fechaCierre, rentabilidad);
    }

    public List<PosicionCerrada> getPosicionesCerradasTopRentabilidad(String jugador, int limite) {
        List<PosicionCerrada> posicionCerradas = new ArrayList<>();
        try {
            ResultSet rs = executeQuery(String.format("SELECT * FROM posicionescerradas WHERE jugador = '%s' ORDER BY rentabilidad DESC LIMIT %d", jugador, limite));
            while (rs.next()) {
                posicionCerradas.add(buildPosicionCerradaByResultset(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return posicionCerradas;
    }

    public List<PosicionCerrada> getPosicionesCerradasTopMenosRentabilidad(String jugador, int limite) {
        List<PosicionCerrada> posicionCerradas = new ArrayList<>();
        try {
            ResultSet rs = executeQuery(String.format("SELECT * FROM posicionescerradas WHERE jugador = '%s' ORDER BY rentabilidad ASC LIMIT %d", jugador, limite));
            while (rs.next()) {
                posicionCerradas.add(buildPosicionCerradaByResultset(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return posicionCerradas;
    }

    public List<PosicionCerrada> getTopRentabilidades(int limite) {
        List<PosicionCerrada> posicionCerradas = new ArrayList<>();
        try {
            ResultSet rs = executeQuery(String.format("SELECT * FROM posicionescerradas ORDER BY rentabilidad DESC LIMIT %d", limite));
            while (rs.next()) {
                posicionCerradas.add(buildPosicionCerradaByResultset(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return posicionCerradas;
    }

    public List<PosicionCerrada> getPosicionesCerradasJugador(String name) {
        List<PosicionCerrada> posicionCerradas = new ArrayList<>();
        try {
            ResultSet rs = executeQuery(String.format("SELECT * FROM posicionescerradas WHERE jugador = '%s'", name));
            while (rs.next()) {
                posicionCerradas.add(buildPosicionCerradaByResultset(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return posicionCerradas;
    }

    private PosicionCerrada buildPosicionCerradaByResultset (ResultSet rs) throws SQLException {
        return new PosicionCerrada(
                rs.getInt("id"),
                rs.getString("jugador"),
                rs.getString("tipo"),
                rs.getString("nombre"),
                rs.getInt("cantidad"),
                rs.getDouble("precioApertura"),
                rs.getString("fechaApertura"),
                rs.getDouble("precioCierre"),
                rs.getString("fechaCierre"),
                rs.getDouble("rentabilidad")
        );
    }

    private int getMaxId(){
        try{
            ResultSet rs = executeQuery("SELECT id FROM posicionescerradas ORDER BY id DESC LIMIT 1");
            while (rs.next()){
                return rs.getInt("id");
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}