package es.serversurvival.bolsa.ordenespremarket.mysql;

import es.serversurvival.shared.mysql.MySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class OrdenesPreMarket extends MySQL {
    public final static OrdenesPreMarket INSTANCE = new OrdenesPreMarket();

    private OrdenesPreMarket() {}

    public void nuevaOrden (String jugador, String nombre_activo, int cantidad, AccionOrden accion_orden, int id_posicionabierta) {
        executeUpdate("INSERT INTO ordenespremarket (jugador, nombre_activo, cantidad, accion_orden, id_posicionabierta) VALUES ('"+jugador+"', '"+nombre_activo+"', '"+cantidad+"', '"+accion_orden.toString()+"', '"+id_posicionabierta+"')");
    }

    public List<OrdenPreMarket> getAllOrdenes () {
        return buildListFromQuery("SELECT * FROM ordenespremarket");
    }

    public List<OrdenPreMarket> getOrdenes (String jugador) {
        return buildListFromQuery("SELECT * FROM ordenespremarket WHERE jugador = '"+jugador+"'");
    }

    public OrdenPreMarket getOrdenTicker (String owner, String ticker) {
        return (OrdenPreMarket) buildObjectFromQuery("SELECT * FROM ordenespremarket WHERE jugador = '" + owner + "' AND nombre_activo = '" + ticker + "'");
    }

    public boolean ordenRegistrada (int id_posicionabierta) {
        return id_posicionabierta != -1 &&! isEmptyFromQuery("SELECT id FROM ordenespremarket WHERE id_posicionabierta = '"+id_posicionabierta+"'");
    }

    public void cambiarNombreJugador (String antiguoNombre, String nuevoNombre) {
        executeUpdate("UPDATE ordenespremarket SET jugador = '"+nuevoNombre+"' WHERE jugador = '"+antiguoNombre+"'");
    }

    public void borrarOrden(int id) {
        executeUpdate("DELETE FROM ordenespremarket WHERE id = '"+id+"'");
    }

    @Override
    protected OrdenPreMarket buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new OrdenPreMarket(rs.getInt("id"),
                rs.getString("jugador"),
                rs.getString("nombre_activo"),
                rs.getInt("cantidad"),
                AccionOrden.valueOf(rs.getString("accion_orden")),
                rs.getInt("id_posicionabierta"));
    }
}
