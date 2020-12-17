package es.serversurvival.mySQL;

import es.serversurvival.mySQL.tablasObjetos.TablaObjeto;
import es.serversurvival.mySQL.tablasObjetos.VerificacionCuenta;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class VerificacionCuentas extends MySQL{

    public static VerificacionCuentas INSTANCE = new VerificacionCuentas();

    private VerificacionCuentas () {}

    public void nuevaVerificacionCuenta (String jugador, int numero) {
        if(getVerificacionCuenta(jugador) != null){
            removeVerificacionCuenta(jugador);
        }

        executeUpdate("INSERT INTO verificacioncuentas (jugador, numero) VALUES ('"+jugador+"', '"+numero+"')");
    }

    public void removeVerificacionCuenta (String jugador) {
        executeUpdate("DELETE FROM verificacioncuentas WHERE jugador = '"+jugador+"'");
    }

    public VerificacionCuenta getVerificacionCuenta (String jugador) {
        ResultSet rs = executeQuery("SELECT * FROM verificacioncuentas WHERE jugador = '"+jugador+"'");

        return (VerificacionCuenta) buildSingleObjectFromResultSet(rs);
    }

    @Override
    protected VerificacionCuenta buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new VerificacionCuenta(rs.getString("jugador"), rs.getInt("numero"));
    }
}
