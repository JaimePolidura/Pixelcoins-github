package es.serversurvival.transacciones.mySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

import es.jaimetruman.insert.Insert;
import es.jaimetruman.select.Select;
import es.jaimetruman.update.Update;
import es.jaimetruman.update.UpdateOptionInitial;
import es.serversurvival._shared.mysql.MySQL;
import es.serversurvival._shared.utils.Funciones;

import static es.serversurvival.transacciones.mySQL.TipoTransaccion.*;

/**
 * 792 -> 600 -> 497 -> 47 (xd)
 */
public final class Transacciones extends MySQL {
    public static final Transacciones INSTANCE = new Transacciones();

    private final UpdateOptionInitial update;

    private Transacciones () {
        this.update = Update.table("transacciones");
    }

    public void nuevaTransaccion(String comprador, String vendedor, double pixelcoins, String objeto, TipoTransaccion tipo) {
        String fecha = LocalDateTime.now().format(Funciones.DATE_FORMATER);

        nuevaTransaccion(new Transaccion(-1, fecha, comprador, vendedor, (int) pixelcoins, objeto, tipo));
    }

    public void nuevaTransaccion(Transaccion transaccion) {
        String query = Insert.table("transacciones")
                .fields("fecha", "comprador", "vendedor", "cantidad", "objeto", "tipo")
                .values(transaccion.getFecha(), transaccion.getComprador(), transaccion.getVendedor(), transaccion.getCantidad(),
                        transaccion.getObjeto(), transaccion.getTipo().toString());

        executeUpdate(query);
    }

    public List<Transaccion> getTransaccionesPagaEmpresa (String jugador) {
        return buildListFromQuery(Select.from("transacciones").where("comprador").equal(jugador).and("tipo").equal(EMPRESA_PAGAR_SALARIO));
    }

    public void setCompradorVendedor (String jugador, String nuevoJugador) {
        executeUpdate(update.set("comprador", nuevoJugador).where("comprador").equal(jugador));
        executeUpdate(update.set("vendedor", nuevoJugador).where("vendedor").equal(jugador));
    }

    public void cambiarNombreJugadorRegistros (String jugadorACambiar, String nuevoNombre) {
        setCompradorVendedor(jugadorACambiar, nuevoNombre);
    }

    @Override
    protected Transaccion buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Transaccion(
                rs.getInt("id"),
                rs.getString("fecha"),
                rs.getString("comprador"),
                rs.getString("vendedor"),
                rs.getInt("cantidad"),
                rs.getString("objeto"),
                valueOf(rs.getString("tipo"))
        );
    }
}
