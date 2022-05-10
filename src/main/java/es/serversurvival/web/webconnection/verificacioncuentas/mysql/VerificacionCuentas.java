package es.serversurvival.web.webconnection.verificacioncuentas.mysql;

import es.jaimetruman.delete.Delete;
import es.jaimetruman.insert.Insert;
import es.jaimetruman.select.Select;
import es.serversurvival._shared.mysql.MySQLRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class VerificacionCuentas extends MySQLRepository {
    public static VerificacionCuentas INSTANCE = new VerificacionCuentas();

    private VerificacionCuentas () {}

    public void nuevaVerificacionCuenta (String jugador, int numero) {
        if(getVerificacionCuenta(jugador) != null){
            removeVerificacionCuenta(jugador);
        }

        executeUpdate(Insert.table("verificacioncuentas")
                .fields("jugador", "numero")
                .values(jugador, numero));
    }

    public void removeVerificacionCuenta (String jugador) {
        executeUpdate(Delete.from("verificacioncuentas").where("jugador").equal(jugador));
    }

    public VerificacionCuenta getVerificacionCuenta (String jugador) {
        return (VerificacionCuenta) buildObjectFromQuery(Select.from("verificacioncuentas").where("jugador").equal(jugador));
    }

    @Override
    protected VerificacionCuenta buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new VerificacionCuenta(rs.getString("jugador"), rs.getInt("numero"));
    }
}
