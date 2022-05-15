package es.serversurvival.deudas._shared.mysql;

import java.sql.*;
import java.util.*;
import java.util.Date;

import es.jaimetruman.delete.Delete;
import es.jaimetruman.insert.Insert;
import es.jaimetruman.select.Select;
import es.jaimetruman.select.SelectOptionInitial;
import es.jaimetruman.update.Update;
import es.jaimetruman.update.UpdateOptionInitial;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.deudas._shared.newformat.domain.Deuda;

import static es.serversurvival._shared.utils.Funciones.*;

// II 325 -> 218 -> 190
public final class Deudas extends MySQLRepository {
    public final static Deudas INSTANCE = new Deudas();
    private final SelectOptionInitial select;
    private final UpdateOptionInitial update;

    private Deudas () {
        this.select = Select.from("deudas");
        this.update = Update.table("deudas");
    }

    public void nuevaDeuda(String deudor, String acredor, int pixelcoins, int tiempo, int interes) {
        String fechaHoy = DATE_FORMATER_LEGACY.format(new Date());
        int cuota = (int) Math.round((double) pixelcoins / tiempo);

        String insertQuery = Insert.table("deudas")
                .fields("deudor", "acredor", "pixelcoins_restantes", "tiempo_restante", "interes", "cuota", "fecha_ultimapaga")
                .values(deudor, acredor, pixelcoins, tiempo, interes, cuota, fechaHoy);

        executeUpdate(insertQuery);
    }

    public Deuda getDeuda(int id){
        return (Deuda) buildObjectFromQuery(select.where("id").equal(id));
    }

    public List<Deuda> getDeudasAcredor(String jugador){
        return buildListFromQuery(select.where("acredor").equal(jugador));
    }

    public List<Deuda> getDeudasDeudor(String jugador){
        return buildListFromQuery(select.where("deudor").equal(jugador));
    }

    public List<Deuda> getAllDeudas () {
        return buildListFromQuery(select);
    }

    public void setPagoDeuda(int id, int pixelcoins, int tiempo, String fecha) {
        executeUpdate(update.set("pixelcoins_restantes", pixelcoins).andSet("tiempo_restante", tiempo).andSet("fecha_ultimapaga", fecha).where("id").equal(id));
    }

    public void setAcredorDeudor (String nombre, String nuevoNombre) {
        executeUpdate(update.set("acredor", nuevoNombre).where("acredor").equal(nombre));
        executeUpdate(update.set("deudor", nuevoNombre).where("deudor").equal(nombre));
    }

    public void borrarDeuda(int id) {
        executeUpdate(Delete.from("deudas").where("id").equal(id));
    }

    public boolean esDeudorDeDeuda(int id, String deudor) {
        return !isEmptyFromQuery(select.where("id").equal(id).and("deudor").equal(deudor));
    }

    public int getAllPixelcoinsDeudasAcredor (String jugador) {
        return getSumaTotalListInteger( getDeudasAcredor(jugador), Deuda::getPixelcoins_restantes);
    }

    public int getAllPixelcoinsDeudasDeudor (String jugador) {
        return getSumaTotalListInteger( getDeudasDeudor(jugador), Deuda::getPixelcoins_restantes);
    }

    public Map<String, List<Deuda>> getAllDeudasAcredorMap () {
        return Funciones.mergeMapList(this.getAllDeudas(), Deuda::getAcredor);
    }

    public Map<String, List<Deuda>> getAllDeudasDeudorMap () {
        return Funciones.mergeMapList(this.getAllDeudas(), Deuda::getDeudor);
    }

    @Override
    protected Deuda buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Deuda(
                UUID.fromString(rs.getString("id")),
                rs.getString("deudor"),
                rs.getString("acredor"),
                rs.getInt("pixelcoins_restantes"),
                rs.getInt("tiempo_restante"),
                rs.getInt("interes"),
                rs.getInt("cuota"),
                rs.getString("fecha_ultimapaga")
        );
    }
}
