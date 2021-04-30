package es.serversurvival.empleados.mysql;

import java.sql.*;
import java.util.*;
import java.util.Date;

import es.serversurvival.empresas.mysql.Empresa;
import es.serversurvival.shared.mysql.MySQL;
import es.serversurvival.utils.Funciones;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static es.serversurvival.utils.Funciones.*;

//II 354 -> 236 -> 156
public final class Empleados extends MySQL {
    public final static Empleados INSTANCE = new Empleados();
    private Empleados () {}

    public void nuevoEmpleado(String empleado, String empresa, double sueldo, TipoSueldo tipo, String cargo) {
        String fechaPaga = dateFormater.format(new Date());

        executeUpdate("INSERT INTO empleados (jugador, empresa, sueldo, cargo, tipo_sueldo, fecha_ultimapaga) VALUES ('" + empleado + "','" + empresa + "','" + sueldo + "','" + cargo + "','" + tipo.codigo + "','" + fechaPaga + "')");
    }

    public Empleado getEmpleado(int id){
        return (Empleado) buildObjectFromQuery(String.format("SELECT * FROM empleados WHERE id = '%d'", id));
    }

    public Empleado getEmpleado (String nombre, String empresa) {
        return (Empleado) buildObjectFromQuery(String.format("SELECT * FROM empleados WHERE jugador = '%s' AND empresa = '%s'", nombre, empresa));
    }

    public void setSueldo(int id, double sueldo) {
        executeUpdate("UPDATE empleados SET sueldo = '"+sueldo+"' WHERE id = '"+id+"'");
    }

    public void setCargo(int id, String cargo) {
        executeUpdate("UPDATE empleados SET cargo = '"+cargo+"' WHERE id = '"+id+"'");
    }

    public void setTipo(int id, TipoSueldo tipo) {
        executeUpdate("UPDATE empleados SET tipo_sueldo = '"+tipo.codigo+"' WHERE id = '"+id+"'");
    }

    public void setFechaPaga(int id, String fechaPaga) {
        executeUpdate("UPDATE empleados SET fecha_ultimapaga = '"+fechaPaga+"' WHERE id = '"+id+"'");
    }

    public void borrarEmplado(int id) {
        executeUpdate("DELETE FROM empleados WHERE id=\"" + id + "\"      ");
    }

    public void setEmpleado (String nombre, String nuevoNombre) {
        executeUpdate("UPDATE FROM empleados SET jugador = '"+nuevoNombre+"' WHERE jugador = '"+nombre+"'");
    }

    public List<Empleado> getAllEmpleados (){
        return buildListFromQuery("SELECT * FROM empleados");
    }

    public List<Empleado> getEmpleadosEmrpesa(String nombreEmpresa){
        return buildListFromQuery("SELECT * FROM empleados WHERE empresa = '"+nombreEmpresa+"'");
    }

    public List<Empleado> getTrabajosJugador(String jugador){
        return buildListFromQuery("SELECT * FROM empleados WHERE jugador = '"+jugador+"'");
    }

    public Map<String, List<Empleado>> getAllEmpleadosEmpresas () {
        return Funciones.mergeMapList(this.getAllEmpleados(), Empleado::getEmpresa);
    }

    public boolean trabajaEmpresa(String empleado, String nombreEmpresa) {
        return !isEmptyFromQuery("SELECT id FROM empleados WHERE jugador = '"+empleado+"' AND empresa = '"+nombreEmpresa+"'");
    }

    public void cambiarEmpresaNombre(String empresa, String nuevoNombre) {
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
