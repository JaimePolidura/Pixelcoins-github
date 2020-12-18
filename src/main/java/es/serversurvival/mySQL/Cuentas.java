package es.serversurvival.mySQL;


import es.serversurvival.mySQL.tablasObjetos.Cuenta;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class Cuentas extends MySQL {
    private Cuentas () {}
    public final static Cuentas INSTANCE = new Cuentas();

    public Cuenta getCuenta(String jugador){
        ResultSet rs = executeQuery("SELECT * FROM cuentas WHERE username = '"+jugador+"'");

        return (Cuenta) buildSingleObjectFromResultSet(rs);
    }

    public Cuenta getCuenta(int id){
        ResultSet rs = executeQuery("SELECT * FROM cuentas WHERE id = '"+id+"'");

        return (Cuenta) buildSingleObjectFromResultSet(rs);
    }

    public boolean estaRegistradoIdCuenta(int id){
        return getCuenta(id) != null;
    }

    public boolean estaRegJugador(String jugador){
        return getCuenta(jugador) != null;
    }
    
    public void setPassword (String username, String password) {
        executeUpdate("UPDATE cuentas SET password = '"+password+"' WHERE username = '"+username+"'");
    }

    public void setUsername (String username, String nuevoUsername) {
        executeUpdate("UPDATE cuentas SET username = '"+nuevoUsername+"' WHERE username = '"+username+"'");
    }

    @Override
    protected Cuenta buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Cuenta(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password")
        );
    }
}
