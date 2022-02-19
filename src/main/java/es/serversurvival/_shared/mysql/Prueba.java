package es.serversurvival._shared.mysql;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

public class Prueba {
    @SneakyThrows
    public static void main(String[] args) {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/sxs_gateway?user=root&password=&useSSL=false&allowPublicKeyRetrieval=true";
        Connection conexion = DriverManager.getConnection(url);
    }
}
