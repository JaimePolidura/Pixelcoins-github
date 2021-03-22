package es.serversurvival.objetos.mySQL;


import es.serversurvival.objetos.mySQL.tablasObjetos.Cuenta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Cuentas extends MySQL {

    public Cuenta getCuenta(String jugador){
        try{
            String consulta = "SELECT * FROM cuentas WHERE username = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, jugador);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                return buildCuentaByResultSet(rs);
            }

        }catch (Exception e){e.printStackTrace();}

        return null;
    }

    public Cuenta getCuenta(int id){
        try{
            String consulta = "SELECT * FROM cuentas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                return buildCuentaByResultSet(rs);
            }
        }catch (Exception e){e.printStackTrace();}

        return null;
    }

    public boolean estaRegistradoIdCuenta(int id){
        return getCuenta(id) != null;
    }

    public boolean estaRegJugador(String jugador){
        return getCuenta(jugador) != null;
    }

    private Cuenta buildCuentaByResultSet (ResultSet rs) throws SQLException {
        return new Cuenta(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password")
        );
    }
}