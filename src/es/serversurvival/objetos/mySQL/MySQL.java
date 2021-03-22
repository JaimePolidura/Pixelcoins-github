package es.serversurvival.objetos.mySQL;

import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQL {
    public static Connection conexion;

    private static final String dbName = "pixelcoins";
    private static final String user = "root";
    private static final String pass = "";

    public Connection conectar() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, user, pass);
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