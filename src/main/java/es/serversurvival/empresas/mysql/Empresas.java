package es.serversurvival.empresas.mysql;

import java.sql.*;
import java.util.*;

import es.jaimetruman.delete.Delete;
import es.jaimetruman.insert.Insert;
import es.jaimetruman.select.Select;
import es.jaimetruman.select.SelectOptionInitial;
import es.jaimetruman.update.Update;
import es.jaimetruman.update.UpdateOptionInitial;
import es.serversurvival.shared.mysql.MySQL;

import es.serversurvival.shared.utils.Funciones;

import static es.serversurvival.shared.utils.Funciones.*;

/**
 * I 610 -> 300
 * II 323 -> 197
 */
public final class Empresas extends MySQL {
    public final static Empresas INSTANCE = new Empresas();

    private final SelectOptionInitial select;
    private final UpdateOptionInitial update;

    private Empresas () {
        this.select = Select.from("empresas");
        this.update = Update.table("empresas");
    }

    public static final int CrearEmpresaNombreLonMax = 16;
    public static final int nMaxEmpresas = 5;
    public static final int CrearEmpresaDescLonMax = 200;

    public void nuevaEmpresa(String nombreEmpresa, String owner, double pixelcoins, double ingresos, double gastos, String icono, String descripcion) {
        String query = Insert.table("empresas")
                .fields("nombre", "owner", "pixelcoins", "ingresos", "gastos", "icono", "descripcion", "cotizada")
                .values(nombreEmpresa, owner, pixelcoins, ingresos, gastos, icono, descripcion, 0);

        executeUpdate(query);
    }

    public Empresa getEmpresa(String empresa){
        return (Empresa) buildObjectFromQuery(select.where("nombre").equal(empresa));
    }

    public Empresa getEmpresa(int id){
        return (Empresa) buildObjectFromQuery(select.where("id").equal(id));
    }

    public List<Empresa> getEmpresasOwner(String owner) {
        return buildListFromQuery(select.where("owner").equal(owner));
    }

    public List<Empresa> getTodasEmpresas() {
        return buildListFromQuery(select);
    }

    public boolean esCotizada (String empresa) {
        return !isEmptyFromQuery(select.where("nombre").equal(empresa).and("cotizada").equal(1));
    }

    public void setOwner(String nombreEmpresa, String nuevoOwner) {
        executeUpdate(update.set("owner", nuevoOwner).where("nombre").equal(nombreEmpresa));
    }

    public void setTodosOwner (String owner, String nuevoOwner) {
        executeUpdate(update.set("owner", nuevoOwner).where("owner").equal(owner));
    }

    public void setPixelcoins(String nombreEmpresa, double pixelcoins) {
        executeUpdate(update.set("pixelcoins", pixelcoins).where("nombre").equal(nombreEmpresa));
    }

    public void setIngresos(String nombreEmpresa, double ingresos) {
        executeUpdate(update.set("ingresos", ingresos).where("nombre").equal(nombreEmpresa));
    }

    public void setGastos(String nombreEmpresa, double gastos) {
        executeUpdate(update.set("gastos", gastos).where("nombre").equal(nombreEmpresa));
    }

    public void setIcono(String nombreEmpresa, String icono) {
        executeUpdate(update.set("icono", icono).where("nombre").equal(nombreEmpresa));
    }

    public void setNombre(String nombreEmpresa, String nuevoNombre) {
        executeUpdate(update.set("nombre", nuevoNombre).where("nombre").equal(nombreEmpresa));
    }

    public void setDescripcion(String nombreEmpresa, String descripcion) {
        executeUpdate(update.set("descripcion", descripcion).where("nombre").equal(nombreEmpresa));
    }

    public void setCotizada (String empresaNombre) {
        executeUpdate(update.set("cotizada", 1).where("nombre").equal(empresaNombre));
    }

    public void borrarEmpresa(String nombreEmpresa) {
        executeUpdate(Delete.from("empresas").where("nombre").equal(nombreEmpresa));
    }

    public double getAllPixelcoinsEnEmpresas (String jugador){
        return getSumaTotalListDouble( getEmpresasOwner(jugador), Empresa::getPixelcoins );
    }

    public Map<String, List<Empresa>> getAllEmpresasJugadorMap () {
        return Funciones.mergeMapList(this.getTodasEmpresas(), Empresa::getOwner);
    }

    @Override
    protected Empresa buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Empresa( rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("owner"),
                rs.getDouble("pixelcoins"),
                rs.getDouble("ingresos"),
                rs.getDouble("gastos"),
                rs.getString("icono"),
                rs.getString("descripcion"),
                rs.getBoolean("cotizada")
        );
    }
}
