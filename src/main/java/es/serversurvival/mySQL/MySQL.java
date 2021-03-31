package es.serversurvival.mySQL;


import es.serversurvival.mySQL.tablasObjetos.TablaObjeto;
import es.serversurvival.util.Funciones;
import lombok.SneakyThrows;

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
    protected static OfertasMercadoServer ofertasMercadoServerMySQL = OfertasMercadoServer.INSTANCE;

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

    @SneakyThrows
    public static Connection conectar() {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/" + dbName + "?user=" + user + "&password=" + pass + "&useSSL=false&allowPublicKeyRetrieval=true";
        conexion = DriverManager.getConnection(url);

        return conexion;
    }

    public static void setConexion (Connection conex){
        conexion = conex;
    }

    @SneakyThrows
    public static void desconectar() {
        conexion.close();
    }

    @SneakyThrows
    protected ResultSet executeQuery(String query) {
        return conexion.createStatement().executeQuery(query);
    }

    @SneakyThrows
    protected void executeUpdate (String consulta) {
        conexion.createStatement().executeUpdate(consulta);
    }

    protected boolean isEmptyFromQuery (String query) {
        try{
            ResultSet rs = executeQuery(query);
            return !rs.next();
        }catch (SQLException e){
            return false;
        }
    }

    protected abstract TablaObjeto buildObjectFromResultSet (ResultSet rs) throws SQLException;

    @SneakyThrows
    protected TablaObjeto buildObjectFromQuery(String query) {
        ResultSet rs = executeQuery(query);
        rs.next();

        return buildObjectFromResultSet(rs);
    }

    protected <T> List<T> buildListFromQuery (String query) {
        ResultSet rs = executeQuery(query);
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
