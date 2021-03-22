package es.serversurvival.objetos.mySQL;


import es.serversurvival.objetos.mySQL.tablasObjetos.NumeroCuenta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NumeroCuentas extends MySQL {

    public void nuevoNumeroCuenta(String jugador){
        try{
            boolean reg = true;
            int numero = 0;
            while (reg){
                numero = (int) (Math.random() * 99999);
                reg = this.estaRegNumeCuenta(numero);
            }

            String consulta = "INSERT INTO numerocuentas (numero, jugador) VALUES (?, ?)";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, numero);
            preparedStatement.setString(2, jugador);
            preparedStatement.executeUpdate();

            Bukkit.getPlayer(jugador).sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD +  "Se te ha generado la numero de cuenta: " + ChatColor.AQUA + "/cuenta");

        }catch (Exception e){e.printStackTrace();}
    }

    public boolean estaRegNumeCuenta(int num){
        try {
            String consulta = "SELECT numero FROM numerocuentas WHERE numero = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setInt(1, num);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean existeNumCuentaPara(String jugador, int num){
        try{
            String consulta = "SELECT numero FROM numerocuentas WHERE jugador = ? AND numero = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, jugador);
            preparedStatement.setInt(2, num);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public NumeroCuenta getNumeroCuenta (String jugador){
        try{
            String consulta = "SELECT * FROM numerocuentas WHERE jugador = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, jugador);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                return new NumeroCuenta(
                        rs.getInt("numero"),
                        rs.getString("jugador")
                );
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
