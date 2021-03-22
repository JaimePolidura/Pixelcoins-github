package es.serversurvival.objetos.mySQL;


import es.serversurvival.objetos.mySQL.tablasObjetos.NumeroCuenta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 89 -> 78
 */
public class NumeroCuentas extends MySQL {

    public void nuevoNumeroCuenta(String jugador){
        boolean reg = true;
        int numero = 0;
        while (reg){
            numero = (int) (Math.random() * 99999);
            reg = this.estaRegNumeCuenta(numero);
        }

        executeUpdate(String.format("INSERT INTO numerocuentas (numero, jugador) VALUES ('%s', '%s')", numero, jugador));

        Bukkit.getPlayer(jugador).sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD +  "Se te ha generado la numero de cuenta: " + ChatColor.AQUA + "/cuenta");
    }

    public NumeroCuenta getNumeroCuenta (String jugador){
        try{
            ResultSet rs = executeQuery(String.format("SELECT * FROM numerocuentas WHERE jugador = '%s'", jugador));

            while (rs.next()){
                return buildNumeroCuentaByResultset(rs);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public boolean estaRegNumeCuenta(int num){
        try {
            ResultSet rs = executeQuery(String.format("SELECT numero FROM numerocuentas WHERE numero = '%d'", num));

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
            ResultSet rs = executeQuery(String.format("SELECT numero FROM numerocuentas WHERE jugador = '%s' AND numero = '%d'", jugador, num));
            while (rs.next()){
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    private NumeroCuenta buildNumeroCuentaByResultset (ResultSet rs) throws SQLException {
        return new NumeroCuenta(
                rs.getInt("numero"),
                rs.getString("jugador")
        );
    }
}