package es.serversurvival.empleados._shared.mysql;

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

//II 354 -> 236 -> 156
public final class Empleados extends MySQLRepository {
    public final static Empleados INSTANCE = new Empleados();
    private final SelectOptionInitial select;
    private final UpdateOptionInitial update;

    private Empleados () {
        this.select = Select.from("empleados");
        this.update = Update.table("empleados");
    }

    public void nuevoEmpleado(String empleado, String empresa, double sueldo, TipoSueldo tipo, String cargo) {
        String fechaPaga = dateFormater.format(new Date());
        String query = Insert.table("empleados")
                .fields("jugador", "empresa", "sueldo", "cargo", "tipo_sueldo", "fecha_ultimapaga")
                .values(empleado, empresa, sueldo, cargo, tipo.toString(), fechaPaga);

        executeUpdate(query);
    }

    public Empleado getEmpleado(int id){
        return (Empleado) buildObjectFromQuery(select.where("id").equal(id));
    }

    public Empleado getEmpleado (String nombre, String empresa) {
        return (Empleado) buildObjectFromQuery(select.where("jugador").equal(nombre).and("empresa").equal(empresa));
    }

    public void setSueldo(int id, double sueldo) {
        executeUpdate(update.set("sueldo", sueldo).where("id").equal(id));
    }

    public void setCargo(int id, String cargo) {
        executeUpdate(update.set("cargo", cargo).where("id").equal(id));
    }

    public void setTipo(int id, TipoSueldo tipo) {
        executeUpdate(update.set("tipo_sueldo", tipo.codigo).where("id").equal(id));
    }

    public void setFechaPaga(int id, String fechaPaga) {
        executeUpdate(update.set("fecha_ultimapaga", fechaPaga).where("id").equal(id));
    }

    public void borrarEmplado(int id) {
        executeUpdate(Delete.from("empleados").where("id").equal(id));
    }

    public void setEmpleado (String nombre, String nuevoNombre) {
        executeUpdate(update.set("jugador", nuevoNombre).where("jugador").equal(nombre));
    }

    public List<Empleado> getAllEmpleados (){
        return buildListFromQuery(select);
    }

    public List<Empleado> getEmpleadosEmrpesa(String nombreEmpresa){
        return buildListFromQuery(select.where("empresa").equal(nombreEmpresa));
    }

    public List<Empleado> getTrabajosJugador(String jugador){
        return buildListFromQuery(select.where("jugador").equal(jugador));
    }

    public Map<String, List<Empleado>> getAllEmpleadosEmpresas () {
        return Funciones.mergeMapList(this.getAllEmpleados(), Empleado::getEmpresa);
    }

    public boolean trabajaEmpresa(String empleado, String nombreEmpresa) {
        return !isEmptyFromQuery(select.where("jugador").equal(empleado).and("empresa").equal(nombreEmpresa));
    }

    public void cambiarEmpresaNombre(String empresa, String nuevoNombre) {
        executeUpdate(update.set("empresa", nuevoNombre).where("empresa").equal(empresa));

        executeUpdate("UPDATE empleados SET empresa = '"+nuevoNombre+"' WHERE empresa = '"+empresa+"'");
    }

    @Override
    protected Empleado buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Empleado( rs.getInt("id"),
                rs.getString("jugador"),
                rs.getString("empresa"),
                rs.getDouble("sueldo"),
                rs.getString("cargo"),
                TipoSueldo.ofCodigo(rs.getString("tipo_sueldo")),
                rs.getString("fecha_ultimapaga"));
    }
}
