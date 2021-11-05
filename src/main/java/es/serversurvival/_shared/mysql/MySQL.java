package es.serversurvival._shared.mysql;


import es.jaimetruman.WriteQuery;
import es.jaimetruman.select.Select;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.*;

public abstract class MySQL implements AllMySQLTablesInstances{
    protected MySQL () {}

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
    protected ResultSet executeQuery(Select query) {
        return conexion.createStatement().executeQuery(query.toString());
    }

    @SneakyThrows
    protected void executeUpdate (WriteQuery consulta) {
        conexion.createStatement().executeUpdate(consulta.toString());
    }

    @SneakyThrows
    protected void executeUpdate (String consulta) {
        conexion.createStatement().executeUpdate(consulta);
    }

    protected boolean isEmptyFromQuery (Select query) {
        try{
            return !executeQuery(query).next();
        }catch (SQLException e){
            return false;
        }
    }

    protected abstract TablaObjeto buildObjectFromResultSet (ResultSet rs) throws SQLException;

    protected TablaObjeto buildObjectFromQuery(Select query) {
        try {
            ResultSet rs = executeQuery(query);
            rs.next();
            return buildObjectFromResultSet(rs);

        } catch (SQLException throwables) {
            return null;
        }
    }

    protected <T> List<T> buildListFromQuery (Select query) {
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
