package es.serversurvival.cuentaweb;

import es.jaimetruman.insert.Insert;
import es.jaimetruman.select.Select;
import es.jaimetruman.select.SelectOptionInitial;
import es.jaimetruman.update.Update;
import es.jaimetruman.update.UpdateOptionInitial;
import es.serversurvival._shared.mysql.MySQL;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class Cuentas extends MySQL {
    public final static Cuentas INSTANCE = new Cuentas();
    private final SelectOptionInitial select;
    private final UpdateOptionInitial update;

    private Cuentas () {
        this.select = Select.from("cuentas");
        this.update = Update.table("cuentas");
    }

    public void nuevaCuenta (String username, String password) {
        String query = Insert.table("cuentas")
                .fields("username", "password", "active", "roles")
                .values(username, password, 1, "USER");

        executeUpdate(query);
    }

    public Cuenta getCuenta(String jugador){
        return (Cuenta) buildObjectFromQuery(select.where("username").equal(jugador));
    }

    public Cuenta getCuenta(int id){
        return (Cuenta) buildObjectFromQuery(select.where("id").equal(id));
    }

    public void setPassword (String username, String password) {
        executeUpdate(update.set("password", password).where("username").equal(username));
    }

    public void setUsername (String username, String nuevoUsername) {
        executeUpdate(update.set("username", nuevoUsername).where("username").equal(username));
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
