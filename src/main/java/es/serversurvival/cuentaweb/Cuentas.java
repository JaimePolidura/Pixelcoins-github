package es.serversurvival.cuentaweb;

import es.serversurvival.shared.mysql.MySQL;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class Cuentas extends MySQL {
    private Cuentas () {}
    public final static Cuentas INSTANCE = new Cuentas();

    public void nuevaCuenta (String username, String password) {
        executeUpdate("INSERT INTO cuentas (username, password, active, roles) VALUES ('"+username+"', '"+password+"', 1, 'USER')");
    }

    public Cuenta getCuenta(String jugador){
        return (Cuenta) buildObjectFromQuery("SELECT * FROM cuentas WHERE username = '"+jugador+"'");
    }

    public Cuenta getCuenta(int id){
        return (Cuenta) buildObjectFromQuery("SELECT * FROM cuentas WHERE id = '"+id+"'");
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
