package es.serversurvival.bolsa._shared.ordenespremarket.mysql;

import es.jaimetruman.delete.Delete;
import es.jaimetruman.insert.Insert;
import es.jaimetruman.select.Select;
import es.jaimetruman.select.SelectOptionInitial;
import es.jaimetruman.update.Update;
import es.serversurvival._shared.mysql.MySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class OrdenesPreMarket extends MySQL {
    public final static OrdenesPreMarket INSTANCE = new OrdenesPreMarket();
    private final SelectOptionInitial select;

    private OrdenesPreMarket() {
        this.select = Select.from("ordenespremarket");
    }

    public void nuevaOrden (String jugador, String nombre_activo, int cantidad, AccionOrden accion_orden, int id_posicionabierta) {
        String query = Insert.table("ordenespremarket")
                .fields("jugador", "nombre_activo", "cantidad", "accion_orden", "id_posicionabierta")
                .values(jugador, nombre_activo, cantidad, accion_orden.toString(), id_posicionabierta);

        System.out.println(query);

        executeUpdate(query);
    }

    public List<OrdenPreMarket> getAllOrdenes () {
        return buildListFromQuery(select);
    }

    public List<OrdenPreMarket> getOrdenes (String jugador) {
        return buildListFromQuery(select.where("jugador").equal(jugador));
    }

    public OrdenPreMarket getOrdenTicker (String owner, String ticker) {
        return (OrdenPreMarket) buildObjectFromQuery(select.where("jugador").equal(owner).and("nombre_activo").equal(ticker));
    }

    public boolean ordenRegistrada (int id_posicionabierta) {
        return id_posicionabierta != -1 && !isEmptyFromQuery(select.where("id_posicionabierta").equal(id_posicionabierta));
    }

    public void cambiarNombreJugador (String antiguoNombre, String nuevoNombre) {
        executeUpdate(Update.table("ordenespremarket").set("jugador", nuevoNombre).where("jugador").equal(antiguoNombre));
    }

    public void borrarOrden(int id) {
        executeUpdate(Delete.from("ordenespremarket").where("id").equal(id));
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
