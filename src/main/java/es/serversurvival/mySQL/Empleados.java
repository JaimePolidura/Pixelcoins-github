package es.serversurvival.mySQL;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import es.serversurvival.mySQL.enums.TRANSACCIONES;
import es.serversurvival.mySQL.tablasObjetos.Deuda;
import es.serversurvival.util.Funciones;
import es.serversurvival.mySQL.tablasObjetos.Empleado;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import javafx.scene.text.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

//II 354 -> 236
public final class Empleados extends MySQL {
    public final static Empleados INSTANCE = new Empleados();
    private Empleados () {}

    private static final List<String> tipoSueldos = Arrays.asList("d", "s", "2s", "m");

    public void nuevoEmpleado(String empleado, String empresa, double sueldo, String tipo, String cargo) {
        String fechaPaga = dateFormater.format(new Date());

        executeUpdate("INSERT INTO empleados (empleado, empresa, sueldo, cargo, tipo, fechaPaga) VALUES ('" + empleado + "','" + empresa + "','" + sueldo + "','" + cargo + "','" + tipo + "','" + fechaPaga + "')");
    }

    public Empleado getEmpleado(int id){
        ResultSet rs = executeQuery(String.format("SELECT * FROM empleados WHERE id = '%d'", id));

        return (Empleado) buildSingleObjectFromResultSet(rs);
    }

    public Empleado getEmpleado (String nombre, String empresa) {
        ResultSet rs = executeQuery(String.format("SELECT * FROM empleados WHERE empleado = '%s' AND empresa = '%s'", nombre, empresa));

        return (Empleado) buildSingleObjectFromResultSet(rs);
    }

    public void setSueldo(int id, double sueldo) {
        executeUpdate("UPDATE empleados SET sueldo = '"+sueldo+"' WHERE id = '"+id+"'");
    }

    public void setCargo(int id, String cargo) {
        executeUpdate("UPDATE empleados SET cargo = '"+cargo+"' WHERE id = '"+id+"'");
    }

    public void setTipo(int id, String tipo) {
        executeUpdate("UPDATE empleados SET tipo = '"+tipo+"' WHERE id = '"+id+"'");
    }

    public void setFechaPaga(int id, String fechaPaga) {
        executeUpdate("UPDATE empleados SET fechaPaga = '"+fechaPaga+"' WHERE id = '"+id+"'");
    }

    public void setEmpresa(int id, String empresa) {
        executeUpdate("UPDATE empleados SET empresa = '"+empresa+"' WHERE id = '"+id+"'");
    }

    public void borrarEmplado(int id) {
        executeUpdate("DELETE FROM empleados WHERE id=\"" + id + "\"      ");
    }

    public void setEmpleado (String nombre, String nuevoNombre) {
        executeUpdate("UPDATE FROM empleados SET empleado = '"+nuevoNombre+"' WHERE empleado = '"+nombre+"'");
    }

    public List<Empleado> getAllEmpleados (){
        ResultSet rs = executeQuery("SELECT * FROM empleados");

        return buildListFromResultSet(rs);
    }

    public List<Empleado> getEmpleadosEmrpesa(String nombreEmpresa){
        ResultSet rs = executeQuery("SELECT * FROM empleados WHERE empresa = '"+nombreEmpresa+"'");

        return buildListFromResultSet(rs);
    }

    public List<Empleado> getTrabajosJugador(String jugador){
        ResultSet rs = executeQuery("SELECT * FROM empleados WHERE empleado = '"+jugador+"'");

        return buildListFromResultSet(rs);
    }

    public boolean trabajaEmpresa(String empleado, String nombreEmpresa) {
        try{
            ResultSet rs = executeQuery("SELECT id FROM empleados WHERE empleado = '"+empleado+"' AND empresa = '"+nombreEmpresa+"'");
            return rs.next();
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public void cambiarEmpresaNombre(String empresa, String nuevoNombre) {
        executeUpdate("UPDATE empleados SET empresa = '"+nuevoNombre+"' WHERE empresa = '"+empresa+"'");
    }

    public void despedir(String nombreEmpresa, String empleado, String razon, Player p) {
        empresasMySQL.conectar();

        int id_emplado = getEmpleado(empleado, nombreEmpresa).getId();
        borrarEmplado(id_emplado);

        p.sendMessage(ChatColor.GOLD + "Has despedido a: " + empleado);
        Player tp = p.getServer().getPlayer(empleado);

        if (tp != null) {
            tp.sendMessage(ChatColor.RED + "Has sido despedido de " + nombreEmpresa + " razon: " + razon);
            tp.playSound(tp.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
        } else {
            mensajesMySQL.nuevoMensaje("", empleado, "Has sido despedido de " + nombreEmpresa + " por: " + razon);
        }
        empresasMySQL.desconectar();
    }

    public void irseEmpresa(String nombreEmpresa, Player p) {
        Empresa empresaAIRse = empresasMySQL.getEmpresa(nombreEmpresa);
        borrarEmplado(getEmpleado(p.getName(), nombreEmpresa).getId());

        p.sendMessage(ChatColor.GOLD + "Te has ido de: " + nombreEmpresa);
        Player ownerPlayer = p.getServer().getPlayer(empresaAIRse.getOwner());
        if (ownerPlayer != null) {
            ownerPlayer.sendMessage(ChatColor.RED + p.getName() + " Se ha ido de tu empresa: " + nombreEmpresa);
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
        } else {
            mensajesMySQL.nuevoMensaje("" ,ownerPlayer.getName(), p.getName() + " se ha ido de tu empresa: " + nombreEmpresa);
        }
    }

    public void editarTipoSueldo (Empresa empresa, Empleado empleado, String tipoSueldo) {
        this.setTipo(empleado.getId(), tipoSueldo);

        double sueldo = empleado.getSueldo();
        String tipoString = toStringTipoSueldo(tipoSueldo);
        Player jugadorAEditarPlayer = Bukkit.getPlayer(empleado.getEmpleado());
        Player sender = Bukkit.getPlayer(empresa.getOwner());

        if (jugadorAEditarPlayer != null) {
            jugadorAEditarPlayer.sendMessage(ChatColor.GOLD + sender.getName() + " te ha cambiado el tiempo por por el que cobras el sueldo, ahora cobras " + ChatColor.GREEN + formatea.format(sueldo) + " PC" + ChatColor.GOLD + " por " + tipoString);
            jugadorAEditarPlayer.playSound(jugadorAEditarPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        } else {
            mensajesMySQL.nuevoMensaje("", empleado.getEmpleado(), "Se te ha cambiado la frecuencia con la que cobras en la empresa: " + empresa.getNombre() + " " + sueldo + " PC/ " + tipoString);
        }
        sender.sendMessage(ChatColor.GOLD + "Has cambiado el tipo de pagado de sueldo de " + empleado.getEmpleado() + " en la empresa " + empresa.getNombre());
    }

    public void editarEmpleadoSueldo(Empresa empresa, Empleado empleado, double nuevoSueldo) {
        setSueldo(empleado.getId(), nuevoSueldo);

        double sueldoAntes = empleado.getSueldo();
        Player sender = Bukkit.getPlayer(empresa.getOwner());
        Player jugadorAEditar = Bukkit.getPlayer(empleado.getEmpleado());
        sender.sendMessage(ChatColor.GOLD + "Has cambiado el sueldo a " + jugadorAEditar + " a " + ChatColor.GREEN + formatea.format(nuevoSueldo) + " PC" + ChatColor.GOLD + " en la empresa: " + empresa);

        if (jugadorAEditar != null) {
            jugadorAEditar.sendMessage(ChatColor.GOLD + sender.getName() + " te ha cambiado el sueldo de " + empresa.getNombre() + " a " + ChatColor.GREEN + formatea.format(nuevoSueldo) + " PC" + ChatColor.GOLD + " antes tenias: " + ChatColor.GREEN + formatea.format(sueldoAntes) + " PC");
            jugadorAEditar.playSound(jugadorAEditar.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        } else {
            mensajesMySQL.nuevoMensaje("", empleado.getEmpleado(), "Se te ha cambiado el sueldo de la empresa: " + empresa.getNombre() + " a " + formatea.format(nuevoSueldo) + " PC antes tenias " + sueldoAntes + " PC");
        }
    }

    public void pagarSueldos() {
        Date hoy = formatFehcaDeHoyException();

        List<Empleado> todosLosEmpleados = getAllEmpleados();
        for (Empleado empl : todosLosEmpleados) {
            Date actualEmpl = formatFechaDeLaBaseDatosException(empl.getFechaPaga());
            String tipoSueldo = empl.getTipo();

            long diferenciaDias = Funciones.diferenciaDias(hoy, actualEmpl);

            if ((tipoSueldo.equalsIgnoreCase("d") && diferenciaDias <= 1) || (tipoSueldo.equalsIgnoreCase("s") && diferenciaDias <= 7) ||
                    (tipoSueldo.equalsIgnoreCase("2s") && diferenciaDias <= 14) || (tipoSueldo.equalsIgnoreCase("m") && diferenciaDias <= 30)) {
                continue;
            }

            boolean sePago = transaccionesMySQL.pagarSalario(empl.getEmpleado(), empl.getEmpresa(), empl.getSueldo());
            if (sePago) {
                setFechaPaga(empl.getId(), dateFormater.format(hoy));
                mensajesMySQL.nuevoMensaje("", empl.getEmpleado(), "Has cobrado " + empl.getSueldo() + " PC de parte de la empresa: " + empl.getEmpresa());
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Se ha pagado " + empl.getSueldo() + " a " + empl.getEmpleado() + " en la empresa: " + empl.getEmpresa());
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "La empresa: " + empl.getEmpresa() + " no ha podido pagar " + empl.getSueldo() + " a " + empl.getEmpleado());
                mensajesMySQL.nuevoMensaje("", empl.getEmpleado(), "No has podido cobrar tu sueldo por parte de " + empl.getEmpresa() + " por que no tiene las suficientes pixelcoins");
            }
        }
    }

    public static boolean esUnTipoDeSueldo (String tipo) {
        return tipoSueldos.stream().anyMatch( (ite) -> ite.equalsIgnoreCase(tipo) );
    }

    public static String toStringTipoSueldo(String tipoSueldo) {
        String nombreTipoSueldo = null;
        switch (tipoSueldo) {
            case "d":
                nombreTipoSueldo = "dia";
                break;
            case "s":
                nombreTipoSueldo = "semana";
                break;
            case "2s":
                nombreTipoSueldo = "2 semanas";
                break;
            case "m":
                nombreTipoSueldo = "mes";
                break;
        }
        return nombreTipoSueldo;
    }

    private Date formatFechaDeLaBaseDatosException (String fecha) {
        try {
            return dateFormater.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Date formatFehcaDeHoyException () {
        try {
            return dateFormater.parse(dateFormater.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected Empleado buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Empleado( rs.getInt("id"),
                rs.getString("empleado"),
                rs.getString("empresa"),
                rs.getDouble("sueldo"),
                rs.getString("cargo"),
                rs.getString("tipo"),
                rs.getString("fechaPaga"));
    }
}
