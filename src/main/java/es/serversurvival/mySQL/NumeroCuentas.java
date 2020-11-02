package es.serversurvival.mySQL;


import com.sun.org.apache.bcel.internal.generic.SWAP;
import es.serversurvival.mySQL.tablasObjetos.Mensaje;
import es.serversurvival.mySQL.tablasObjetos.NumeroCuenta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 89 -> 78 -> 51
 */
public final class NumeroCuentas extends MySQL {
    public final static NumeroCuentas INSTANCE = new NumeroCuentas();
    private NumeroCuentas () {}

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
        ResultSet rs = executeQuery(String.format("SELECT * FROM numerocuentas WHERE jugador = '%s'", jugador));

        return (NumeroCuenta) buildSingleObjectFromResultSet(rs);
    }

    public boolean estaRegNumeCuenta(int num){
        ResultSet rs = executeQuery(String.format("SELECT numero FROM numerocuentas WHERE numero = '%d'", num));

        return !isEmpty(rs);
    }

    public boolean existeNumCuentaPara(String jugador, int num){
        try{
            ResultSet rs = executeQuery(String.format("SELECT numero FROM numerocuentas WHERE jugador = '%s' AND numero = '%d'", jugador, num));
            return rs.next();
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public void setJugador (String jugador, String nuevoJugador) {
        executeUpdate("UPDATE cuentas SET jugador = '"+nuevoJugador+"' WHERE jugador = '"+jugador+"'");
    }

    @Override
    protected NumeroCuenta buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new NumeroCuenta(
                rs.getInt("numero"),
                rs.getString("jugador")
        );
    }
}