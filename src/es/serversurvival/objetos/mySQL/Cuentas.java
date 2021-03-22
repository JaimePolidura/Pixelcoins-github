package es.serversurvival.objetos.mySQL;


import es.serversurvival.objetos.mySQL.tablasObjetos.Cuenta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Cuentas extends MySQL {

    public void nuevaIDCuenta(String jugador){
        try{
            boolean reg = true;
            int id = 0;
            while (reg){
                id = (int) (Math.random() * 99999);
                reg = this.estaRegistradoIdCuenta(id);
            }

            String consulta = "INSERT INTO cuentas (id, jugador) VALUES (?, ?)";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, jugador);
            preparedStatement.executeUpdate();

            Bukkit.getPlayer(jugador).sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD +  "Se te ha generado la id de cuenta: " + ChatColor.AQUA + "/cuenta");

        }catch (Exception e){e.printStackTrace();}
    }

    private boolean estaRegistradoIdCuenta(int id){
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
            String consulta = "SELECT id FROM cuentas WHERE jugador = ?";
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
            String consulta = "SELECT * FROM cuentas WHERE jugador = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, jugador);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                return new Cuenta(
                        rs.getInt("id"),
                        rs.getString("jugador"),
                        rs.getString("contra"),
                        rs.getString("email")
                );
            }

        }catch (Exception e){e.printStackTrace();}

        return null;
    }
}