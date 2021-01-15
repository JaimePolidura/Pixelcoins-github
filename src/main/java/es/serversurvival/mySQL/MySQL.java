package es.serversurvival.mySQL;


import es.serversurvival.mySQL.tablasObjetos.TablaObjeto;
import es.serversurvival.util.Funciones;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class MySQL {
    protected MySQL () {}

    protected DecimalFormat formatea = Funciones.FORMATEA;
    protected SimpleDateFormat dateFormater = Funciones.DATE_FORMATER;

    protected static Cuentas cuentasMySQL = Cuentas.INSTANCE;
    protected static Empleados empleadosMySQL = Empleados.INSTANCE;
    protected static Empresas empresasMySQL = Empresas.INSTANCE;
    protected static Encantamientos encantamientosMySQL = Encantamientos.INSTANCE;
    protected static Jugadores jugadoresMySQL = Jugadores.INSTANCE;
    protected static LlamadasApi llamadasApiMySQL = LlamadasApi.INSTANCE;
    protected static Mensajes mensajesMySQL = Mensajes.INSTANCE;
    protected static Ofertas ofertasMySQL = Ofertas.INSTANCE;
    protected static PosicionesAbiertas posicionesAbiertasMySQL = PosicionesAbiertas.INSTANCE;
    protected static PosicionesCerradas posicionesCerradasMySQL = PosicionesCerradas.INSTANCE;
    protected static Transacciones transaccionesMySQL = Transacciones.INSTANCE;
    protected static Deudas deudasMySQL = Deudas.INSTANCE;

    protected static Connection conexion;
    /** ----------========= LINUX =========----------
    private static final String dbName = "pixelcoins";
    private static final String user = "java";
    private static final String pass = "Pixelcoins123_";
    //CONTEASEÑA MYSQL -> Pixelcoins123
    //NUEVA CONTEASEÑA MYSQL -> Pixelcoins123_*/

    private static final String dbName = "pixelcoins2";
    private static final String user = "root";
    private static final String pass = "";

    public static Connection conectar() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/" + dbName + "?user=" + user + "&password=" + pass + "&useSSL=false&allowPublicKeyRetrieval=true";
            conexion = DriverManager.getConnection(url);

            return conexion;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setConexion (Connection conex){
        conexion = conex;
    }

    public static Connection conectar(String driver) {
        try {
            Class.forName(driver);
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306?serverTimezone=UTC/" + dbName, user, pass);
            return conexion;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void desconectar() {
        try {
            conexion.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected ResultSet executeQuery(String query) {
        try{
            return conexion.createStatement().executeQuery(query);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    protected void executeUpdate (String consulta) {
        try{
            conexion.createStatement().executeUpdate(consulta);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    protected boolean isEmpty (ResultSet rs) {
        try{
            boolean next = rs.next();
            System.out.println(next); //¿wtf?

            return rs.next();
        }catch (SQLException e){
            return false;
        }
    }

    protected abstract TablaObjeto buildObjectFromResultSet (ResultSet rs) throws SQLException;

    protected TablaObjeto buildSingleObjectFromResultSet (ResultSet rs) {
        try{
            rs.next();
            return buildObjectFromResultSet(rs);
        }catch (SQLException e) {
            return null;
        }
    }

    protected <T> List<T> buildListFromResultSet (ResultSet rs) {
        List<T> list = new ArrayList<>();
        try{
            while (rs.next()){
                list.add((T) buildObjectFromResultSet(rs));
            }
        }catch (SQLException e){
            return Collections.EMPTY_LIST;
        }
        return list;
    }
}
