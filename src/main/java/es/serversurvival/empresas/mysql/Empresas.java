package es.serversurvival.empresas.mysql;

import java.sql.*;
import java.util.*;

import es.serversurvival.shared.mysql.MySQL;

import es.serversurvival.utils.Funciones;

import static es.serversurvival.utils.Funciones.*;

/**
 * I 610 -> 300
 * II 323 -> 197
 */
public final class Empresas extends MySQL {
    public final static Empresas INSTANCE = new Empresas();
    private Empresas () {}

    public static final int CrearEmpresaNombreLonMax = 16;
    public static final int nMaxEmpresas = 5;
    public static final int CrearEmpresaDescLonMax = 200;

    public void nuevaEmpresa(String nombreEmpresa, String owner, double pixelcoins, double ingresos, double gastos, String icono, String descripcion) {
        executeUpdate("INSERT INTO empresas (nombre, owner, pixelcoins, ingresos, gastos, icono, descripcion, cotizada) VALUES ('" + nombreEmpresa + "','" + owner + "','" + pixelcoins + "','" + ingresos + "','" + gastos + "','" + icono + "','" + descripcion + "', 0)");
    }

    public Empresa getEmpresa(String empresa){
        return (Empresa) buildObjectFromQuery("SELECT * FROM empresas WHERE nombre = '"+empresa+"'");
    }

    public Empresa getEmpresa(int id){
        return (Empresa) buildObjectFromQuery("SELECT * FROM empresas WHERE id = '"+id+"'");
    }

    public List<Empresa> getEmpresasOwner(String owner) {
        return buildListFromQuery("SELECT * FROM empresas WHERE owner = '"+owner+"'");
    }

    public List<Empresa> getTodasEmpresas() {
        return buildListFromQuery("SELECT * FROM empresas");
    }

    public boolean esCotizada (String empresa) {
        return !isEmptyFromQuery("SELECT * FROM empresas WHERE nombre = '"+empresa+"' AND cotizada = 1");
    }

    public void setOwner(String nombreEmpresa, String nuevoOwner) {
        executeUpdate("UPDATE empresas SET owner = '"+nuevoOwner+"' WHERE nombre = '"+nombreEmpresa+"'");
    }

    public void setTodosOwner (String owner, String nuevoOwner) {
        executeUpdate("UPDATE empresas SET owner = '"+nuevoOwner+"' WHERE owner = '"+owner+"'");
    }

    public void setPixelcoins(String nombreEmpresa, double pixelcoins) {
        executeUpdate("UPDATE empresas SET pixelcoins = '"+pixelcoins+"' WHERE nombre = '"+nombreEmpresa+"'");
    }

    public void setIngresos(String nombreEmpresa, double ingresos) {
        executeUpdate("UPDATE empresas SET ingresos = '"+ingresos+"' WHERE nombre = '"+nombreEmpresa+"'");
    }

    public void setGastos(String nombreEmpresa, double gastos) {
        executeUpdate("UPDATE empresas SET gastos = '"+gastos+"' WHERE nombre = '"+nombreEmpresa+"'");
    }

    public void setIcono(String nombreEmpresa, String icono) {
        executeUpdate("UPDATE empresas SET icono = '"+icono+"' WHERE nombre = '"+nombreEmpresa+"'");
    }

    public void setNombre(String nombreEmpresa, String nuevoNombre) {
        executeUpdate("UPDATE empresas SET nombre = '"+nuevoNombre+"' WHERE nombre = '"+nombreEmpresa+"'");
    }

    public void setDescripcion(String nombreEmpresa, String descripcion) {
        executeUpdate("UPDATE empresas SET descripcion = '"+descripcion+"' WHERE nombre = '"+nombreEmpresa+"'");
    }

    public void setCotizada (String empresaNombre) {
        executeUpdate("UPDATE empresas SET cotizada = 1 WHERE nombre = '"+empresaNombre+"'");
    }

    public void borrarEmpresa(String nombreEmpresa) {
        executeUpdate(String.format("DELETE FROM empresas WHERE nombre = '%s'", nombreEmpresa));
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
