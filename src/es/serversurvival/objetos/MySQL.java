package es.serversurvival.objetos;

import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQL {
    public static Connection conexion;

    public void conectar(String user, String pass, String dbName) throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, user, pass);
        } catch (ClassNotFoundException e) {
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, e);
        } catch (SQLException e) {
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception e) {
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    //Metodo para desconectar
    public void desconectar() {
        try {
            conexion.close();
        } catch (SQLException e) {
            Logger.getLogger(Deudas.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}