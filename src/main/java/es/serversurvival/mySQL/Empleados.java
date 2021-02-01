package es.serversurvival.mySQL;

import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.Date;

import com.mojang.datafixers.types.Func;
import es.serversurvival.mySQL.enums.TipoSueldo;
import es.serversurvival.util.Funciones;
import es.serversurvival.mySQL.tablasObjetos.Empleado;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static es.serversurvival.util.Funciones.*;

//II 354 -> 236
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
        List<Empleado> allEmpleados = this.getAllEmpleados();
        Map<String, List<Empleado>> toReturn = new HashMap<>();

        allEmpleados.forEach(empleado -> {
            if(toReturn.get(empleado.getEmpresa()) == null){
                toReturn.put(empleado.getEmpresa(), listOf(empleado));
            }else{
                List<Empleado> empleadosEmpresa = toReturn.get(empleado.getEmpresa());
                empleadosEmpresa.add(empleado);

                toReturn.replace(empleado.getEmpresa(), empleadosEmpresa);
            }
        });

        return toReturn;
    }

    public boolean trabajaEmpresa(String empleado, String nombreEmpresa) {
        return !isEmptyFromQuery("SELECT id FROM empleados WHERE jugador = '"+empleado+"' AND empresa = '"+nombreEmpresa+"'");
    }

    public void cambiarEmpresaNombre(String empresa, String nuevoNombre) {
        executeUpdate("UPDATE empleados SET empresa = '"+nuevoNombre+"' WHERE empresa = '"+empresa+"'");
    }

    public void despedir(String nombreEmpresa, String empleado, String razon, Player ownerPlayer) {
        int id_emplado = getEmpleado(empleado, nombreEmpresa).getId();
        borrarEmplado(id_emplado);

        ownerPlayer.sendMessage(ChatColor.GOLD + "Has despedido a: " + empleado);

        String mensajeOnline = ChatColor.RED + "Has sido despedido de " + nombreEmpresa + " razon: " + razon;
        String mensajeOffline = "Has sido despedido de " + nombreEmpresa + " por: " + razon;
        enviarMensaje(empleado, mensajeOnline, mensajeOnline, Sound.BLOCK_ANVIL_LAND, 10, 1);
    }

    public void irseEmpresa (String nombreEmpresa, Player player) {
        Empresa empresaAIRse = empresasMySQL.getEmpresa(nombreEmpresa);
        borrarEmplado(getEmpleado(player.getName(), nombreEmpresa).getId());

        player.sendMessage(ChatColor.GOLD + "Te has ido de: " + nombreEmpresa);

        String mensajeOnline = ChatColor.RED + player.getName() + " Se ha ido de tu empresa: " + nombreEmpresa;
        String mensajeOffline = player.getName() + " se ha ido de tu empresa: " + nombreEmpresa;
        enviarMensaje(empresaAIRse.getOwner(), mensajeOnline, mensajeOffline, Sound.BLOCK_ANVIL_LAND, 10, 1);
    }

    public void pagarSueldos() {
        Date hoy = formatFehcaDeHoyException();

        List<Empleado> todosLosEmpleados = getAllEmpleados();
        for (Empleado empl : todosLosEmpleados) {
            Date actualEmpl = formatFechaDeLaBaseDatosException(empl.getFecha_ultimapaga());
            String tipoSueldo = empl.getTipo_sueldo().codigo;

            int diferenciaDias = (int) diferenciaDias(hoy, actualEmpl);

            if(TipoSueldo.dentroDeLosDias(tipoSueldo, diferenciaDias)){
               continue;
            }

            boolean sePago = transaccionesMySQL.pagarSalario(empl.getJugador(), empl.getEmpresa(), empl.getSueldo());

            if (sePago) {
                setFechaPaga(empl.getId(), dateFormater.format(hoy));
                mensajesMySQL.nuevoMensaje("", empl.getJugador(), "Has cobrado " + empl.getSueldo() + " PC de parte de la empresa: " + empl.getEmpresa());
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Se ha pagado " + empl.getSueldo() + " a " + empl.getJugador() + " en la empresa: " + empl.getEmpresa());
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "La empresa: " + empl.getEmpresa() + " no ha podido pagar " + empl.getSueldo() + " a " + empl.getJugador());
                mensajesMySQL.nuevoMensaje("", empl.getJugador(), "No has podido cobrar tu sueldo por parte de " + empl.getEmpresa() + " por que no tiene las suficientes pixelcoins");
            }
        }
    }

    @SneakyThrows
    private Date formatFechaDeLaBaseDatosException (String fecha) {
        return dateFormater.parse(fecha);
    }

    @SneakyThrows
    private Date formatFehcaDeHoyException () {
        return dateFormater.parse(dateFormater.format(new Date()));
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
