package es.serversurvival.objetos.mySQL;


import es.serversurvival.objetos.mySQL.tablasObjetos.Cuenta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Cuentas extends MySQL {

    /*public void confirmarCuenta(Cuenta cuenta){
        try{
            String consulta = "UPDATE cuentas SET password = ? WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, cuenta.getPassword());
            preparedStatement.setString(2, cuenta.getEmail());
            preparedStatement.setInt(3, cuenta.getId());
            preparedStatement.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

    public boolean estaRegistradoIdCuenta(int id){
        try{
            String consuta = "SELECT id FROM cuentas WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consuta);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                return true;
            }
        }catch (Exception e){e.printStackTrace();}
        return false;
    }

    public boolean estaRegJugador(String jugador){
        try{
            String consulta = "SELECT id FROM cuentas WHERE username = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, jugador);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public Cuenta getCuenta(String jugador){
        try{
            String consulta = "SELECT * FROM cuentas WHERE username = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, jugador);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                return new Cuenta(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
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
                return new Cuenta(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        }catch (Exception e){e.printStackTrace();}

        return null;
    }
}