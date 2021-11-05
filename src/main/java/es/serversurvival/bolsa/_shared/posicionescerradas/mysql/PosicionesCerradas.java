package es.serversurvival.bolsa._shared.posicionescerradas.mysql;

import es.jaimetruman.insert.Insert;
import es.jaimetruman.select.Select;
import es.jaimetruman.select.SelectOptionInitial;
import es.jaimetruman.update.Update;
import es.serversurvival.bolsa._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival._shared.mysql.MySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static es.jaimetruman.select.Order.*;

/**
 * 331 -> 114
 * II: 114 -> 67
 */
public final class PosicionesCerradas extends MySQL {
    public final static PosicionesCerradas INSTANCE = new PosicionesCerradas();

    private final SelectOptionInitial select;

    private PosicionesCerradas () {
        this.select = Select.from("posicionescerradas");
    }

    public void nuevaPosicion(String jugador, TipoActivo tipoActivo, String nombre, int cantidad, double precioApertura, String fechaApertura, double precioCierre, String valorNombre, double rentabilidad, TipoPosicion tipoPosicion) {
        String fechaCierre = dateFormater.format(new Date());
        String query = Insert.table("posicionescerradas")
                .fields("jugador", "tipo_activo", "simbolo", "cantidad", "precio_apertura", "fecha_apertura", "precio_cierre", "fecha_cierre", "rentabilidad", "nombre_activo", "tipo_posicion")
                .values(jugador, tipoActivo, nombre, cantidad, precioApertura, fechaApertura, precioCierre, fechaCierre, rentabilidad, valorNombre, tipoPosicion);

        executeUpdate(query);
    }

    public List<PosicionCerrada> getPosicionesCerradasTopRentabilidad(String jugador, int limite) {
        return buildListFromQuery(select.where("jugador").equal(jugador).orderBy("rentabilidad", DESC).limit(limite));
    }

    public List<PosicionCerrada> getPosicionesCerradasTopMenosRentabilidad(String jugador, int limite) {
        return buildListFromQuery(select.where("jugador").equal(jugador).orderBy("rentabilidad", ASC).limit(limite));
    }

    public List<PosicionCerrada> getTopRentabilidades() {
        return buildListFromQuery(select.orderBy("rentabilidad", DESC));
    }

    public List<PosicionCerrada> getPeoresRentabilidades() {
        return buildListFromQuery(select.orderBy("rentabilidad", ASC));
    }

    public List<PosicionCerrada> getPosicionesCerradasJugador(String name) {
        return buildListFromQuery(select.where("jugador").equal(name));
    }

    public void setJugador (String jugador, String nuevoJugador) {
        executeUpdate(Update.table("posicionescerradas").set("jugador", nuevoJugador).where("jugador").equal(jugador));
    }

    @Override
    protected PosicionCerrada buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new PosicionCerrada(
                rs.getInt("id"),
                rs.getString("jugador"),
                TipoActivo.valueOf(rs.getString("tipo_activo")),
                rs.getString("simbolo"),
                rs.getInt("cantidad"),
                rs.getDouble("precio_apertura"),
                rs.getString("fecha_apertura"),
                rs.getDouble("precio_cierre"),
                rs.getString("fecha_cierre"),
                rs.getDouble("rentabilidad"),
                rs.getString("nombre_activo"),
                TipoPosicion.valueOf(rs.getString("tipo_posicion"))
        );
    }
}
