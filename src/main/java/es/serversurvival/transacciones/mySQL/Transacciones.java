package es.serversurvival.transacciones.mySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import es.serversurvival.shared.mysql.MySQL;

/**
 * 792 -> 600 -> 497 -> 47 (xd)
 */
public final class Transacciones extends MySQL {
    public static final Transacciones INSTANCE = new Transacciones();

    public void nuevaTransaccion(Transaccion transaccion) {
        String fecha = transaccion.getFecha();

        executeUpdate("INSERT INTO transacciones (fecha, comprador, vendedor, cantidad, objeto, tipo) VALUES ('" + fecha + "','" + transaccion.getComprador() + "','" + transaccion.getVendedor() + "','" + transaccion.getCantidad() + "','" + transaccion.getObjeto() + "','" + transaccion.getTipo().toString() + "')");
    }

    public List<Transaccion> getTransaccionesPagaEmpresa (String jugador) {
        return buildListFromQuery("SELECT * FROM transacciones WHERE comprador = '"+jugador+"' AND tipo = '"+ TipoTransaccion.EMPRESA_PAGAR_SALARIO +"'");
    }

    public void setCompradorVendedor (String jugador, String nuevoJugador) {
        executeUpdate("UPDATE transacciones SET comprador = '"+nuevoJugador+"' WHERE comprador = '"+jugador+"'");
        executeUpdate("UPDATE transacciones SET vendedor = '"+nuevoJugador+"' WHERE vendedor = '"+jugador+"'");
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
                TipoTransaccion.valueOf(rs.getString("tipo"))
        );
    }
}
