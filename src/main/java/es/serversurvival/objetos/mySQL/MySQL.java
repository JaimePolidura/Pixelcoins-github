package es.serversurvival.objetos.mySQL;

import java.sql.Connection;
import java.sql.DriverManager;
public class MySQL {
    public static Connection conexion;

    private static final String dbName = "pixelcoins";
    private static final String user = "root";
    private static final String pass = "";

    public static void setConexion (Connection conex){
        conexion = conex;
    }

    public Connection conectar() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, user, pass);
            //conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC/" + dbName, user, pass);

            return conexion;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Connection conectar(String driver) {
        try {
            Class.forName(driver);
            conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306?serverTimezone=UTC/" + dbName, user, pass);
            return conexion;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Metodo para desconectar
    public void desconectar() {
        try {
            conexion.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}